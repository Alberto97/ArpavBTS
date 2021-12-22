package org.alberto97.arpavbts.fragments

import android.os.Bundle
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager

abstract class MapClusterBaseFragment<T : ClusterItem> : MapBaseFragment() {

    private lateinit var _clusterManager: ClusterManager<T>
    val clusterManager get() = _clusterManager

    override fun onMapReady(mapViewBundle: Bundle?) {
        _clusterManager = ClusterManager(requireContext(), googleMap)
    }
}