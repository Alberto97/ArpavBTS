package org.alberto97.arpavbts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.CameraUpdateFactory
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
import org.alberto97.arpavbts.tools.IOperatorConfig
import org.alberto97.arpavbts.ui.MarkerRenderer
import org.alberto97.arpavbts.viewmodels.MapViewModel
import javax.inject.Inject

object MapRequestKey {
    const val PICK_OPERATOR = "PICK_OPERATOR"
}

object PickOperatorResultKey {
    const val OPERATOR = "operator"
}

@AndroidEntryPoint
class MapFragment : MapClusterBaseFragment<ClusterItemData>() {

    private val viewModel: MapViewModel by activityViewModels()
    private lateinit var binding: FragmentMapBinding

    @Inject
    lateinit var operatorConfig: IOperatorConfig

    // Bottom Behavior
    private lateinit var btsBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var gestoreBehavior: BottomSheetBehavior<LinearLayout>

    private val clusterBts = arrayListOf<ClusterItemData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(MapRequestKey.PICK_OPERATOR) { _, bundle ->
            val result = bundle.getString(PickOperatorResultKey.OPERATOR)
            viewModel.selectOperator(result)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setupWithNavController(findNavController())
        binding.toolbar.setOnMenuItemClickListener {
            onMenuItemClick(it)
        }

        fabSetup()
        btsBottomSheetSetup()
        gestoreBottomSheetSetup()
    }

    private fun fabSetup() {
        binding.fab.setOnClickListener {
            viewModel.clearOperator()
        }

        viewModel.selectedOperator.observe(viewLifecycleOwner) { operator ->
            if (operator.isNullOrEmpty())
                binding.fab.hide()
            else
                binding.fab.show()
        }
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
        val renderer = MarkerRenderer(requireContext(), getMap(), getClusterManager(), operatorConfig)
        renderer.minClusterSize = 2
        getClusterManager().renderer = renderer

        if (mapViewBundle == null) {
            val lastPosition = viewModel.getLastCameraPosition()
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(lastPosition)
            getMap().moveCamera(cameraUpdate)
        }

        // Setup various listeners
        getClusterManager().setOnClusterClickListener { cluster -> onClusterClick(cluster) }
        getClusterManager().setOnClusterItemClickListener { item -> onClusterItemClick(item) }
        getMap().setOnCameraIdleListener {
            viewModel.setCameraPosition(getMap().cameraPosition)
            getClusterManager().onCameraIdle()
        }
        getMap().setOnMapClickListener { onMapClick() }
    }

    private fun onMapClick() {
        showBtsBottomBehavior(false)
        showGestoreBottomBehavior(false)
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
        btsBehavior.state = handleBottomSheetVisibility(show)
    }

    private fun showGestoreBottomBehavior(show: Boolean) {
        gestoreBehavior.state = handleBottomSheetVisibility(show)
    }

    private fun handleBottomSheetVisibility(show: Boolean): Int {
        return if (show) {
            showFab(false)
            BottomSheetBehavior.STATE_EXPANDED
        } else {
            showFab(true)
            BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun showFab(show: Boolean) {
        if (!isFilterActive()) return
        if (show)
            binding.fab.show()
        else
            binding.fab.hide()
    }

    private fun isFilterActive(): Boolean {
        return !viewModel.selectedOperator.value.isNullOrEmpty()
    }

    private fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_providers -> {
                findNavController().navigate(R.id.action_map_to_operators_pref)
                true
            }
            R.id.action_map_reset -> {
                val defaultPosition = viewModel.defaultCameraPosition
                val cameraUpdate = CameraUpdateFactory.newCameraPosition(defaultPosition)
                getMap().animateCamera(cameraUpdate)
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