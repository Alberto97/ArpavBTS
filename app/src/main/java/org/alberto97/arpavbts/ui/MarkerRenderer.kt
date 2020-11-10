package org.alberto97.arpavbts.ui

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.models.ClusterItemData
import org.alberto97.arpavbts.tools.Extensions.toHue
import org.alberto97.arpavbts.tools.IGestoriUtils

class MarkerRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<ClusterItemData>,
    private val utils: IGestoriUtils
) :
    DefaultClusterRenderer<ClusterItemData>(context, map, clusterManager) {

    private val buckets = arrayOf(10, 20, 50, 100, 200, 500, 1000)
    private val colors = arrayOf(
        R.color.cluster_0,
        R.color.cluster_1,
        R.color.cluster_2,
        R.color.cluster_3,
        R.color.cluster_4,
        R.color.cluster_5,
        R.color.cluster_6
    )

    override fun onBeforeClusterItemRendered(item: ClusterItemData, markerOptions: MarkerOptions) {
        val color = utils.getColor(item.data.gestore).toHue()
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color))
        super.onBeforeClusterItemRendered(item, markerOptions)
    }

    override fun getColor(clusterSize: Int): Int {
        for (i in buckets.indices) {
            if (clusterSize < buckets[i])
                return ContextCompat.getColor(context, colors[i])
        }

        return ContextCompat.getColor(context, colors.last())
    }
}