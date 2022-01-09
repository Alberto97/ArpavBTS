package org.alberto97.arpavbts.tools

import android.os.Bundle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.runBlocking
import org.alberto97.arpavbts.ui.DefaultCameraSettings
import javax.inject.Inject
import javax.inject.Singleton

interface MapStateManager {
    fun getMapState(): Bundle
    fun getCameraPosition(): CameraPosition
    suspend fun setCameraPosition(cameraPosition: CameraPosition)
}

@Singleton
class MapStateManagerImpl @Inject constructor(private val mapStateStored: IMapStateStored) : MapStateManager {

    override fun getMapState(): Bundle {
        val position = getCameraPosition()
        val camera = Bundle().apply {
            putParcelable("camera", position)
        }
        val state = Bundle().apply {
            putBundle("map_state", camera)
        }
        return state
    }

    override fun getCameraPosition(): CameraPosition {
        return getPersistedCameraPosition() ?: DefaultCameraSettings.cameraPosition
    }

    private fun getPersistedCameraPosition(): CameraPosition? {
        val value = runBlocking { mapStateStored.getMapState() } ?: return null
        val lastPosition = LatLng(value.lat, value.lon)
        return CameraPosition.Builder()
            .target(lastPosition)
            .zoom(value.zoom)
            .build()
    }

    override suspend fun setCameraPosition(cameraPosition: CameraPosition) {
        val target = cameraPosition.target
        val zoom = cameraPosition.zoom
        mapStateStored.setMapState(target.latitude, target.longitude, zoom)
    }
}