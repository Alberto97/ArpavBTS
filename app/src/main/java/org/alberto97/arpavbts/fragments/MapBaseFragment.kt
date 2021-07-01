package org.alberto97.arpavbts.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.maps.android.ktx.awaitMap

const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

//
// https://github.com/googlemaps/android-samples/blob/main/ApiDemos/kotlin/app/src/gms/java/com/example/kotlindemos/RawMapViewDemoActivity.kt
//
abstract class MapBaseFragment : Fragment() {

    abstract fun getMapView(): MapView
    private lateinit var mMap: GoogleMap

    abstract fun onMapReady(mapViewBundle: Bundle?)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeMap(savedInstanceState)
    }

    private fun initializeMap(savedInstanceState: Bundle?) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        val mapViewBundle = savedInstanceState?.getBundle(MAPVIEW_BUNDLE_KEY)
        getMapView().onCreate(mapViewBundle)

        lifecycle.coroutineScope.launchWhenCreated {
            mMap = getMapView().awaitMap()
            onMapReady(mapViewBundle)
        }
    }

    fun getMap(): GoogleMap {
        return mMap
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY) ?: Bundle().also {
            outState.putBundle(MAPVIEW_BUNDLE_KEY, it)
        }
        getMapView().onSaveInstanceState(mapViewBundle)
    }

    override fun onStart() {
        super.onStart()
        getMapView().onStart()
    }

    override fun onStop() {
        super.onStop()
        getMapView().onStop()
    }

    override fun onPause() {
        getMapView().onPause()
        super.onPause()
    }

    override fun onResume() {
        getMapView().onResume()
        super.onResume()
    }

    override fun onDestroy() {
        getMapView().onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        getMapView().onLowMemory()
    }
}