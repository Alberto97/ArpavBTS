package org.alberto97.arpavbts

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import org.alberto97.arpavbts.GestoriUtils
import org.alberto97.arpavbts.models.ClusterItemData

class MarkerRenderer(context: Context, map: GoogleMap, clusterManager: ClusterManager<ClusterItemData>) :
    DefaultClusterRenderer<ClusterItemData>(context, map, clusterManager) {

    private val utils = GestoriUtils()

    override fun onBeforeClusterItemRendered(item: ClusterItemData?, markerOptions: MarkerOptions?) {
        item ?: return
        val color = utils.getColorForMarker(item)
        markerOptions!!.icon(BitmapDescriptorFactory.defaultMarker(color))
        super.onBeforeClusterItemRendered(item, markerOptions)
    }
}