package org.alberto97.arpavbts.fragments

import android.os.Bundle
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager

abstract class MapClusterBaseFragment<T : ClusterItem> : MapBaseFragment() {

    private lateinit var clusterManager: ClusterManager<T>

    override fun onMapReady(mapViewBundle: Bundle?) {
        clusterManager = ClusterManager(requireContext(), getMap())
    }

    fun getClusterManager(): ClusterManager<T> {
        return clusterManager
    }
}