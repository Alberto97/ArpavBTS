package org.alberto97.arpavbts.viewmodels

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.google.android.gms.maps.model.CameraPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.db.Bts
import org.alberto97.arpavbts.db.IBtsRepository
import org.alberto97.arpavbts.models.BTSDetailsAdapterItem
import org.alberto97.arpavbts.models.ClusterItemData
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.tools.IOperatorConfig
import org.alberto97.arpavbts.tools.MapStateManager
import org.alberto97.arpavbts.workers.DownloadWorker
import org.alberto97.arpavbts.workers.DownloadWorkerConstants
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val app: Application,
    private val btsRepo: IBtsRepository,
    private val operatorConfig: IOperatorConfig,
    private val mapStateManager: MapStateManager
) : ViewModel() {
    companion object {
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

    private val _selectedOperator = MutableLiveData<String?>(null)
    val selectedOperator: LiveData<String?> = _selectedOperator

    val btsList: LiveData<List<ClusterItemData>> = _selectedOperator.switchMap { carrier ->
        btsRepo.getBts(carrier).map { list ->
            list.map { item -> ClusterItemData(item) }
        }.asLiveData()
    }

    val btsDataTitle = MutableLiveData<String>()
    val btsData = MutableLiveData<List<BTSDetailsAdapterItem>>()
    val gestoreData = MutableLiveData<List<GestoreAdapterItem>>()

    init {
        updateDb()
    }

    fun clearOperator() {
        _selectedOperator.value = null
    }

    fun selectOperator(id: String?) {
        _selectedOperator.value = id
    }

    fun setBtsData(data: Bts) {
        btsDataTitle.value = data.nome

        val tempList = arrayListOf(
            BTSDetailsAdapterItem(R.drawable.ic_code_black_24dp, data.codice),
            BTSDetailsAdapterItem(R.drawable.ic_person_black_24dp, data.gestore),
            BTSDetailsAdapterItem(R.drawable.ic_terrain_black_24dp, "${data.quotaSlm} m"),
            BTSDetailsAdapterItem(R.drawable.ic_place_black_24dp, data.indirizzo),
            BTSDetailsAdapterItem(R.drawable.ic_find_in_page_black_24dp, data.postazione)
        )

        // Remove missing info
        val list = tempList.filter { !it.text.isNullOrEmpty() && it.text != "NO DATA" }
        btsData.value = list
    }

    fun setGestoreData(data: List<ClusterItemData>) {
        val list = data.map {
            GestoreAdapterItem(
                operatorConfig.getColor(it.data.gestore),
                it.data.nome,
                it.data.idImpianto.toString()
            )
        }
        gestoreData.value = list
    }

    fun updateDb(forceUpdate: Boolean = false) {
        val data = Data.Builder().apply {
            putBoolean(DownloadWorkerConstants.FORCE_UPDATE, forceUpdate)
        }
        val downloadWork = OneTimeWorkRequestBuilder<DownloadWorker>().apply {
            setInputData(data.build())
            setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        }
        WorkManager.getInstance(app).enqueue(downloadWork.build())
    }

    fun saveMapState(data: Bundle) {
        state[MAPVIEW_BUNDLE_KEY] = data
    }

    fun restoreMapState(): Bundle {
        return state.get<Bundle>(MAPVIEW_BUNDLE_KEY) ?: mapStateManager.getMapState()
    }

    fun setCameraPosition(cameraPosition: CameraPosition) {
        viewModelScope.launch {
            mapStateManager.setCameraPosition(cameraPosition)
        }
    }
}