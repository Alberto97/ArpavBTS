package org.alberto97.arpavbts.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.adapters.BTSAdapter
import org.alberto97.arpavbts.adapters.GestoreAdapter
import org.alberto97.arpavbts.databinding.ActivityMapBinding
import org.alberto97.arpavbts.db.Bts
import org.alberto97.arpavbts.models.BTSDetailsAdapterItem
import org.alberto97.arpavbts.models.ClusterItemData
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.tools.GestoreResult
import org.alberto97.arpavbts.tools.GestoriUtils
import org.alberto97.arpavbts.tools.all
import org.alberto97.arpavbts.ui.GestoreBottomSheetDialog
import org.alberto97.arpavbts.ui.MarkerRenderer
import org.alberto97.arpavbts.viewmodels.MapViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapActivity : MapBaseActivity(), GoogleMap.OnMapClickListener,
    GestoreResult,
    ClusterManager.OnClusterClickListener<ClusterItemData>, ClusterManager.OnClusterItemClickListener<ClusterItemData> {

    private lateinit var clusterManager: ClusterManager<ClusterItemData>

    // Bottom Behavior
    private lateinit var btsBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var gestoreBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var binding: ActivityMapBinding
    private val viewModel: MapViewModel by viewModel()

    private var clusterBts = listOf<ClusterItemData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_map
        )
        setSupportActionBar(binding.toolbar)


        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        btsBehavior = BottomSheetBehavior.from(binding.btsBottomSheet)
        btsBehavior.peekHeight = 0
        btsBehavior.isHideable = true
        hideBtsBottomBehavior()

        gestoreBehavior = BottomSheetBehavior.from(binding.gestoreBottomSheet)
        gestoreBehavior.peekHeight = 0
        gestoreBehavior.isHideable = true
        hideGestoreBottomBehavior()

        viewModel.btsList.observe(this, Observer {
            setMarkers(it)
        })

        binding.btsRecyclerView.adapter = BTSAdapter()
        binding.gestoreRecyclerView.adapter = GestoreAdapter { out -> onBtsClick(out) }
    }

    private fun setMarkers(btsList: List<ClusterItemData>) {
        if (!this::clusterManager.isInitialized) return
        with(clusterManager) {
            clearItems()
            addItems(btsList)
            cluster() // Draw clusters
        }
    }

    override fun onMapReady() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(45.6736317,11.9941753), 7f))
        getMap().setOnMapClickListener(this)

        clusterManager = ClusterManager(this, getMap())
        val renderer = MarkerRenderer(this, getMap(), clusterManager)
        renderer.minClusterSize = 1
        clusterManager.renderer = renderer
        clusterManager.setOnClusterClickListener(this)
        clusterManager.setOnClusterItemClickListener(this)
        getMap().setOnCameraIdleListener(clusterManager)
        getMap().setOnMarkerClickListener(clusterManager)

        // Fetch data
        onGestoreResult(all)
    }

    override fun onMapClick(p0: LatLng?) {
        hideBtsBottomBehavior()
        hideGestoreBottomBehavior()
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

    override fun onClusterItemClick(marker: ClusterItemData?): Boolean {
        marker ?: return false
        getMap().animateCamera(CameraUpdateFactory.newLatLng(marker.position))

        // If there's a title, cluster is a marker
        if (marker.title.isNotEmpty()) {
            setBtsBottom(marker.data)
        }

        return true
    }

    override fun onGestoreResult(id: String) {
        viewModel.getBtsByCarrier(id)
    }

    private fun setGestoreBottom(data: List<ClusterItemData>) {
        clusterBts = data
        hideBtsBottomBehavior()
        showGestoreBottomBehavior()

        val utils = GestoriUtils()
        val list = data.map {
            GestoreAdapterItem(
                utils.getColor(it.data.gestore),
                it.data.nome,
                it.data.idImpianto.toString()
            )
        }

        val adapter = binding.gestoreRecyclerView.adapter as GestoreAdapter
        adapter.submitList(list)
    }

    private fun onBtsClick(out: GestoreAdapterItem) {
        val bts = clusterBts.find { it.data.idImpianto == out.id.toInt()}
        hideGestoreBottomBehavior()
        bts ?: return
        setBtsBottom(bts.data)
    }

    private fun setBtsBottom(data: Bts) {
        hideGestoreBottomBehavior()
        showBtsBottomBehavior()

        // Bottom bar setup
        binding.btsName.text = data.nome

        val tempList = arrayListOf(
            BTSDetailsAdapterItem(R.drawable.ic_code_black_24dp, data.codice),
            BTSDetailsAdapterItem(R.drawable.ic_person_black_24dp, data.gestore),
            BTSDetailsAdapterItem(R.drawable.ic_terrain_black_24dp, "${data.quotaSlm} m"),
            BTSDetailsAdapterItem(R.drawable.ic_place_black_24dp, data.indirizzo),
            BTSDetailsAdapterItem(R.drawable.ic_find_in_page_black_24dp, data.postazione),
            BTSDetailsAdapterItem(
                R.drawable.ic_settings_input_antenna_black_24dp,
                data.pontiRadio
            )
        )

        // Remove missing infos
        val list = tempList.filter { !it.text.isNullOrEmpty() && it.text != "NO DATA" }
        val adapter = binding.btsRecyclerView.adapter as BTSAdapter
        adapter.submitList(list)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_gestori -> {
                GestoreBottomSheetDialog()
                    .showNow(supportFragmentManager, "")
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