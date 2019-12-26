package org.alberto97.arpavbts.tools

import android.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import org.alberto97.arpavbts.db.Bts
import org.alberto97.arpavbts.models.BTSData
import org.alberto97.arpavbts.models.ClusterItemData

interface IGestoriUtils {
    fun getColorForMarker(gestore: String): Float
    fun getColorForImage(gestore: String): Int
}

class GestoriUtils : IGestoriUtils {

    override fun getColorForMarker(gestore: String): Float {
        return when(gestore) {
            iliad -> BitmapDescriptorFactory.HUE_RED
            tim -> BitmapDescriptorFactory.HUE_AZURE
            vodafone -> BitmapDescriptorFactory.HUE_RED
            windTre -> BitmapDescriptorFactory.HUE_ORANGE
            else -> BitmapDescriptorFactory.HUE_GREEN
        }
    }

    override fun getColorForImage(gestore: String): Int {
        return when(gestore) {
            iliad -> Color.parseColor("#d32f2f")
            tim -> Color.parseColor("#29B6F6")
            vodafone -> Color.parseColor("#ff3434")
            windTre -> Color.parseColor("#ffa000")
            all -> Color.parseColor("#EEEEEE")
            else -> Color.parseColor("#64dd17")
        }
    }
}