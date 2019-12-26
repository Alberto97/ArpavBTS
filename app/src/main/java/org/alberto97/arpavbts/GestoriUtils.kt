package org.alberto97.arpavbts

import android.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import org.alberto97.arpavbts.models.BTSData
import org.alberto97.arpavbts.models.ClusterItemData

class GestoriUtils {

    fun getColorForMarker(item: ClusterItemData): Float {
        return when(item.data.gestore) {
            iliad -> BitmapDescriptorFactory.HUE_RED
            tim -> BitmapDescriptorFactory.HUE_AZURE
            vodafone -> BitmapDescriptorFactory.HUE_RED
            windTre -> BitmapDescriptorFactory.HUE_ORANGE
            else -> BitmapDescriptorFactory.HUE_GREEN
        }
    }

    fun getColorForImage(gestore: String): Int {
        return when(gestore) {
            iliad -> Color.parseColor("#d32f2f")
            tim -> Color.parseColor("#29B6F6")
            vodafone -> Color.parseColor("#ff3434")
            windTre -> Color.parseColor("#ffa000")
            all -> Color.parseColor("#EEEEEE")
            else -> Color.parseColor("#64dd17")
        }
    }

    fun getColorForImage(data: BTSData): Int {
        return getColorForImage(data.gestore)
    }
}