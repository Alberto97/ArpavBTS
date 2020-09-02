package org.alberto97.arpavbts.ui

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import org.alberto97.arpavbts.models.ClusterItemData
import org.alberto97.arpavbts.tools.Extensions.toHue
import org.alberto97.arpavbts.tools.IGestoriUtils
import org.koin.core.KoinComponent
import org.koin.core.inject

class MarkerRenderer(context: Context, map: GoogleMap, clusterManager: ClusterManager<ClusterItemData>) :
    DefaultClusterRenderer<ClusterItemData>(context, map, clusterManager), KoinComponent {

    private val utils: IGestoriUtils by inject()

    override fun onBeforeClusterItemRendered(item: ClusterItemData, markerOptions: MarkerOptions) {
        val color = utils.getColor(item.data.gestore).toHue()
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color))
        super.onBeforeClusterItemRendered(item, markerOptions)
    }
}