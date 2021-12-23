package org.alberto97.arpavbts.ui

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.maps.MapView

// Handles the lifecycle of a MapView and save its state across process death
class MapViewLifecycleHandler(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val mapView: MapView,
    private val restoreMapState: () -> Bundle?,
    private val saveMapState: (data: Bundle) -> Unit,
) : DefaultLifecycleObserver, ComponentCallbacks {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
        context.registerComponentCallbacks(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        val mapViewBundle = restoreMapState()
        mapView.onCreate(mapViewBundle)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        mapView.onStart()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        mapView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        mapView.onPause()
        super.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        persistMapState()
        mapView.onStop()
        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        lifecycleOwner.lifecycle.removeObserver(this)
        context.unregisterComponentCallbacks(this)
        mapView.onDestroy()
        super.onDestroy(owner)
    }

    private fun persistMapState() {
        val bundle = Bundle().also { bundle ->
            mapView.onSaveInstanceState(bundle)
        }
        saveMapState(bundle)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        // Noop
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
    }
}
