package org.alberto97.arpavbts.ui

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistry
import com.google.android.gms.maps.MapView

// Handles the lifecycle of a MapView and save its state across process death
class MapViewLifecycleHandler(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val savedStateRegistry: SavedStateRegistry,
    private val mapView: MapView
) : DefaultLifecycleObserver, ComponentCallbacks {
    companion object {
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

    init {
        lifecycleOwner.lifecycle.addObserver(this)
        context.registerComponentCallbacks(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        val mapViewBundle = savedStateRegistry.consumeRestoredStateForKey(MAPVIEW_BUNDLE_KEY)
        mapView.onCreate(mapViewBundle)

        savedStateRegistry.registerSavedStateProvider(MAPVIEW_BUNDLE_KEY) {
            Bundle().also { bundle ->
                mapView.onSaveInstanceState(bundle)
            }
        }
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
        mapView.onStop()
        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        // When using the Navigation AAC the SavedStateProvider is not being called and it remains in the queue.
        // This leads to IllegalArgumentException(SavedStateProvider with the given key is already registered)
        // the next time onCreate is hit (eg. on back navigation).
        savedStateRegistry.unregisterSavedStateProvider(MAPVIEW_BUNDLE_KEY)

        lifecycleOwner.lifecycle.removeObserver(this)
        context.unregisterComponentCallbacks(this)
        mapView.onDestroy()
        super.onDestroy(owner)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        // Noop
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
    }
}
