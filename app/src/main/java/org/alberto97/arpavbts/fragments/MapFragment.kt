package org.alberto97.arpavbts.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.Cluster
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.adapters.BTSAdapter
import org.alberto97.arpavbts.adapters.GestoreAdapter
import org.alberto97.arpavbts.databinding.FragmentMapBinding
import org.alberto97.arpavbts.db.Bts
import org.alberto97.arpavbts.models.ClusterItemData
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.tools.Extensions.isNightModeOn
import org.alberto97.arpavbts.tools.IGestoriUtils
import org.alberto97.arpavbts.ui.GestoreBottomSheetDialog
import org.alberto97.arpavbts.ui.MarkerRenderer
import org.alberto97.arpavbts.ui.SHEET_SELECTED_PROVIDER
import org.alberto97.arpavbts.viewmodels.MapViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : MapClusterBaseFragment<ClusterItemData>() {

    private val viewModel: MapViewModel by viewModels()
    private lateinit var binding: FragmentMapBinding

    @Inject
    lateinit var gestoriUtils: IGestoriUtils

    // Bottom Behavior
    private lateinit var btsBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var gestoreBehavior: BottomSheetBehavior<LinearLayout>

    private val clusterBts = arrayListOf<ClusterItemData>()

    private val selectGestoreRequestCode = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater)

        binding.toolbar.setupWithNavController(findNavController())
        binding.toolbar.setOnMenuItemClickListener {
            onMenuItemClick(it)
        }

        btsBottomSheetSetup()
        gestoreBottomSheetSetup()

        return binding.root
    }

    private fun btsBottomSheetSetup() {
        btsBehavior = BottomSheetBehavior.from(binding.btsBottomSheet)
        binding.btsRecyclerView.adapter = BTSAdapter()

        // Title
        viewModel.btsDataTitle.observe(viewLifecycleOwner, {
            binding.btsName.text = it
        })

        // Recyclerview refresh
        viewModel.btsData.observe(viewLifecycleOwner, {
            val adapter = binding.btsRecyclerView.adapter as BTSAdapter
            adapter.submitList(it)
        })
    }

    private fun gestoreBottomSheetSetup() {
        gestoreBehavior = BottomSheetBehavior.from(binding.gestoreBottomSheet)
        binding.gestoreRecyclerView.adapter = GestoreAdapter { out -> onBtsClick(out) }

        // Recyclerview refresh
        viewModel.gestoreData.observe(viewLifecycleOwner, {
            val adapter = binding.gestoreRecyclerView.adapter as GestoreAdapter
            adapter.submitList(it)
        })
    }

    private fun setMarkers(btsList: List<ClusterItemData>) {
        with(getClusterManager()) {
            clearItems()
            addItems(btsList)
            cluster() // Draw clusters
        }
    }

    override fun getMapView() = binding.map

    override fun onMapReady(mapViewBundle: Bundle?) {
        super.onMapReady(mapViewBundle)

        if (requireContext().resources.configuration.isNightModeOn()) {
            val mapStyle = MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.night_map)
            getMap().setMapStyle(mapStyle)
        }

        viewModel.btsList.observe(viewLifecycleOwner, {
            setMarkers(it)
        })

        // Setup custom marker renderer for multiple marker colors
        val renderer = MarkerRenderer(requireContext(), getMap(), getClusterManager(), gestoriUtils)
        renderer.minClusterSize = 2
        getClusterManager().renderer = renderer

        if (mapViewBundle == null) {
            // Move camera to Veneto
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(45.6736317,11.9941753), 7f))
        }

        // Setup various listeners
        getClusterManager().setOnClusterClickListener { cluster -> onClusterClick(cluster) }
        getClusterManager().setOnClusterItemClickListener { item -> onClusterItemClick(item) }
        getMap().setOnCameraIdleListener(getClusterManager())
        getMap().setOnMapClickListener { onMapClick() }

        // Fetch data
        onGestoreResult(null)
    }

    private fun onMapClick() {
        showBtsBottomBehavior(false)
        showGestoreBottomBehavior(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == selectGestoreRequestCode && resultCode == Activity.RESULT_OK) {
            val id = data?.getStringExtra(SHEET_SELECTED_PROVIDER)
            onGestoreResult(id)
        }
    }

    private fun onGestoreResult(id: String?) {
        viewModel.getBtsByCarrier(id)
    }

    private fun onClusterClick(cluster: Cluster<ClusterItemData>?): Boolean {
        cluster ?: return false

        if (cluster.size > 50) {
            val nextZoomLevel = getMap().cameraPosition.zoom + 1.0f
            getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(cluster.position, nextZoomLevel))
            return true
        }

        getMap().animateCamera(CameraUpdateFactory.newLatLng(cluster.position))
        val res = cluster.items.toList()
        setGestoreBottom(res)
        return true
    }

    private fun onClusterItemClick(item: ClusterItemData?): Boolean {
        item ?: return false
        getMap().animateCamera(CameraUpdateFactory.newLatLng(item.position))

        // If there's a title, cluster is a marker
        if (item.title.isNotEmpty()) {
            setBtsBottom(item.data)
        }

        return true
    }

    private fun setGestoreBottom(data: List<ClusterItemData>) {
        clusterBts.clear()
        clusterBts.addAll(data)

        showBtsBottomBehavior(false)
        viewModel.setGestoreData(data)

        lifecycleScope.launch {
            delay(500L)
            showGestoreBottomBehavior(true)
        }
    }

    private fun onBtsClick(out: GestoreAdapterItem) {
        val bts = clusterBts.find { it.data.idImpianto == out.id?.toInt()}
        showGestoreBottomBehavior(false)
        bts ?: return
        setBtsBottom(bts.data)
    }

    private fun setBtsBottom(data: Bts) {
        showGestoreBottomBehavior(false)
        viewModel.setBtsData(data)

        lifecycleScope.launch {
            delay(500L)
            showBtsBottomBehavior(true)
        }

        // Position
        /*bts_position.setOnClickListener {
            val latitude = data.latitudine
            val longitude = data.longitudine
            val title = data.title
            val uri = "geo:0,0?q=$latitude,$longitude($title)"
            val gmmIntentUri = Uri.parse(uri)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            //mapIntent.`package` = "com.google.android.apps.maps"
            if (mapIntent.resolveActivity(activity!!.packageManager) != null) {
                startActivity(mapIntent)
            }
        }*/

    }

    private fun showBtsBottomBehavior(show: Boolean) {
        btsBehavior.state = if (show) {
            BottomSheetBehavior.STATE_EXPANDED
        } else {
            BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun showGestoreBottomBehavior(show: Boolean) {
        gestoreBehavior.state = if (show) {
            BottomSheetBehavior.STATE_EXPANDED
        } else {
            BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_providers -> {
                val dialog = GestoreBottomSheetDialog()
                dialog.setTargetFragment(this, selectGestoreRequestCode)
                dialog.showNow(parentFragmentManager, "")
                true
            }
            R.id.action_update -> {
                viewModel.updateDb(forceUpdate = true)
                true
            }
            R.id.action_about -> {
                findNavController().navigate(R.id.action_map_to_about)
                true
            }
            else -> false
        }
    }
}