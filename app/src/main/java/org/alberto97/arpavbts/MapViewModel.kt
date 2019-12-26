package org.alberto97.arpavbts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.alberto97.arpavbts.models.BTSData
import org.alberto97.arpavbts.models.ClusterItemData

class MapViewModel(app: Application) : AndroidViewModel(app) {

    val btsList: MutableLiveData<List<ClusterItemData>> by lazy {
        MutableLiveData<List<ClusterItemData>>()
    }

    private val btsRepo = BtsRepository.instance(app)

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