package org.alberto97.arpavbts.ui

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

object DefaultCameraSettings {
    private val venetoPosition = LatLng(45.6736317, 11.9941753)
    val cameraPosition = CameraPosition.Builder()
        .target(venetoPosition)
        .zoom(7f)
        .build()
}
