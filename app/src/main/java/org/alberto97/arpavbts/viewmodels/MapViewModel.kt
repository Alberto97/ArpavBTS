package org.alberto97.arpavbts.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import org.alberto97.arpavbts.db.Bts
import org.alberto97.arpavbts.db.IBtsRepository
import org.alberto97.arpavbts.models.ClusterItemData
import org.alberto97.arpavbts.tools.all

class MapViewModel(private val btsRepo: IBtsRepository) : ViewModel() {

    private val _carrierInput = MutableLiveData<String>()
    private val _btsList: LiveData<List<Bts>> = Transformations.switchMap(_carrierInput) {
        if (it == all)
            btsRepo.getBts()
        else
            btsRepo.getBts(it)
    }

    val btsList: LiveData<List<ClusterItemData>> = Transformations.map(_btsList) {
        list -> list.map { ClusterItemData(it) }
    }

    fun getBtsByCarrier(id: String) {
        _carrierInput.value = id
    }

//    fun clearDb() {
//        viewModelScope.launch {
//            btsRepo.clear()
//        }
//    }
//
//    fun updateDb() {
//        viewModelScope.launch {
//            btsRepo.updateBts()
//        }
//    }
}