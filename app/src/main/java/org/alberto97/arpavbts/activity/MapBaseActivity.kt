package org.alberto97.arpavbts.activity

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

abstract class MapBaseActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        onMapReady()
    }

    @Suppress("unused")
    fun getMap(): GoogleMap {
        return mMap
    }

    abstract fun onMapReady()
}