package org.alberto97.arpavbts.models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import org.alberto97.arpavbts.db.Bts

class ClusterItemData(val data: Bts) : ClusterItem {

    override fun getPosition(): LatLng {
        return LatLng(data.latitudine.toDouble(), data.longitudine.toDouble())
    }

    override fun getTitle(): String {
        return data.codice
    }

    override fun getSnippet(): String {
        return data.idImpianto.toString()
    }

    override fun getZIndex(): Float? = null
}