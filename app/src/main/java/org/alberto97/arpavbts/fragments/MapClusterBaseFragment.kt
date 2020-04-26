package org.alberto97.arpavbts.fragments

import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager

abstract class MapClusterBaseFragment<T : ClusterItem> : MapBaseFragment() {

    private lateinit var clusterManager: ClusterManager<T>

    override fun onMapReady() {
        clusterManager = ClusterManager(requireContext(), getMap())
    }

    fun getClusterManager(): ClusterManager<T> {
        return clusterManager
    }
}