package org.alberto97.arpavbts.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.adapters.BTSAdapter
import org.alberto97.arpavbts.adapters.GestoreAdapter
import org.alberto97.arpavbts.databinding.FragmentMapBinding
import org.alberto97.arpavbts.db.Bts
import org.alberto97.arpavbts.models.ClusterItemData
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.tools.all
import org.alberto97.arpavbts.ui.GestoreBottomSheetDialog
import org.alberto97.arpavbts.ui.MarkerRenderer
import org.alberto97.arpavbts.ui.SHEET_SELECTED_GESTORE_ID
import org.alberto97.arpavbts.viewmodels.MapViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : MapClusterBaseFragment<ClusterItemData>(),
    GoogleMap.OnMapClickListener,
    ClusterManager.OnClusterClickListener<ClusterItemData>,
    ClusterManager.OnClusterItemClickListener<ClusterItemData> {

    private val viewModel: MapViewModel by viewModel()
    private lateinit var binding: FragmentMapBinding

    // Bottom Behavior
    private lateinit var btsBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var gestoreBehavior: BottomSheetBehavior<LinearLayout>

    private val clusterBts = arrayListOf<ClusterItemData>()

    private val selectGestoreRequestCode = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater)

        btsBottomSheetSetup()
        gestoreBottomSheetSetup()

        return binding.root
    }

    private fun btsBottomSheetSetup() {
        // Bottom sheet
        btsBehavior = BottomSheetBehavior.from(binding.btsBottomSheet)
        btsBehavior.peekHeight = 0
        btsBehavior.isHideable = true
        hideBtsBottomBehavior()

        // Adapter
        binding.btsRecyclerView.adapter = BTSAdapter()

        // Title
        viewModel.btsDataTitle.observe(viewLifecycleOwner, Observer {
            binding.btsName.text = it
        })

        // Recyclerview refresh
        viewModel.btsData.observe(viewLifecycleOwner, Observer {
            val adapter = binding.btsRecyclerView.adapter as BTSAdapter
            adapter.submitList(it)
        })
    }

    private fun gestoreBottomSheetSetup() {
        // Bottom sheet
        gestoreBehavior = BottomSheetBehavior.from(binding.gestoreBottomSheet)
        gestoreBehavior.peekHeight = 0
        gestoreBehavior.isHideable = true
        hideGestoreBottomBehavior()

        // Adapter
        binding.gestoreRecyclerView.adapter = GestoreAdapter { out -> onBtsClick(out) }

        // Recyclerview refresh
        viewModel.gestoreData.observe(viewLifecycleOwner, Observer {
            val adapter = binding.gestoreRecyclerView.adapter as GestoreAdapter
            adapter.submitList(it)
        })
    }

    override fun getToolbar() = binding.toolbar
    override fun getMenuResource() = R.menu.main

    private fun setMarkers(btsList: List<ClusterItemData>) {
        with(getClusterManager()) {
            clearItems()
            addItems(btsList)
            cluster() // Draw clusters
        }
    }

    override fun getMapView() = binding.map

    override fun onMapReady() {
        super.onMapReady()

        viewModel.btsList.observe(viewLifecycleOwner, Observer {
            setMarkers(it)
        })

        // Setup custom marker renderer for multiple marker colors
        val renderer = MarkerRenderer(requireContext(), getMap(), getClusterManager())
        renderer.minClusterSize = 1
        getClusterManager().renderer = renderer

        // Move camera to Veneto
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(45.6736317,11.9941753), 7f))

        // Setup various listeners
        getClusterManager().setOnClusterClickListener(this)
        getClusterManager().setOnClusterItemClickListener(this)
        getMap().setOnCameraIdleListener(getClusterManager())
        getMap().setOnMarkerClickListener(getClusterManager())
        getMap().setOnMapClickListener(this)

        // Fetch data
        onGestoreResult(all)
    }

    override fun onMapClick(p0: LatLng?) {
        hideBtsBottomBehavior()
        hideGestoreBottomBehavior()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == selectGestoreRequestCode && resultCode == Activity.RESULT_OK) {
            val id = data?.getStringExtra(SHEET_SELECTED_GESTORE_ID) ?: return
            onGestoreResult(id)
        }
    }

    private fun onGestoreResult(id: String) {
        viewModel.getBtsByCarrier(id)
    }

    override fun onClusterClick(cluster: Cluster<ClusterItemData>?): Boolean {
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

    override fun onClusterItemClick(item: ClusterItemData?): Boolean {
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

        hideBtsBottomBehavior()
        viewModel.setGestoreData(data)

        Handler().postDelayed({
            showGestoreBottomBehavior()
        }, 500)
    }

    private fun onBtsClick(out: GestoreAdapterItem) {
        val bts = clusterBts.find { it.data.idImpianto == out.id.toInt()}
        hideGestoreBottomBehavior()
        bts ?: return
        setBtsBottom(bts.data)
    }

    private fun setBtsBottom(data: Bts) {
        hideGestoreBottomBehavior()
        viewModel.setBtsData(data)

        Handler().postDelayed({
            showBtsBottomBehavior()
        }, 500)

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

    private fun hideBtsBottomBehavior() {
        btsBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        btsBehavior.peekHeight = 0
    }

    private fun hideGestoreBottomBehavior() {
        gestoreBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        gestoreBehavior.peekHeight = 0
    }

    private fun showBtsBottomBehavior() {
        btsBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun showGestoreBottomBehavior() {
        gestoreBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_gestori -> {
                val dialog = GestoreBottomSheetDialog()
                dialog.setTargetFragment(this, selectGestoreRequestCode)
                dialog.showNow(parentFragmentManager, "")
                true
            }
            R.id.action_update -> {
                viewModel.updateDb()
                true
            }
            else ->
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                super.onOptionsItemSelected(item)
        }
    }
}