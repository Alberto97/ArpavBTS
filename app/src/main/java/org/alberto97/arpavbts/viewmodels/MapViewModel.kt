package org.alberto97.arpavbts.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.alberto97.arpavbts.repositories.IBtsRepository
import org.alberto97.arpavbts.tools.all
import org.alberto97.arpavbts.models.BTSData
import org.alberto97.arpavbts.models.ClusterItemData

class MapViewModel(private val btsRepo: IBtsRepository) : ViewModel() {

    val btsList: MutableLiveData<List<ClusterItemData>> by lazy {
        MutableLiveData<List<ClusterItemData>>()
    }

    init {
        loadData(btsRepo.get())
    }

    private fun loadData(originalList: List<BTSData>) {
        btsList.value = originalList.map { ClusterItemData(it) }
    }

    fun getBtsByCarrier(id: String) {
        val list = if (id == all)
            btsRepo.get()
        else
            btsRepo.get(id)

        loadData(list)
    }
}