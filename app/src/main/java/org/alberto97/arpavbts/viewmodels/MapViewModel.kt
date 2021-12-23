package org.alberto97.arpavbts.viewmodels

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import androidx.work.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.db.Bts
import org.alberto97.arpavbts.db.IBtsRepository
import org.alberto97.arpavbts.models.BTSDetailsAdapterItem
import org.alberto97.arpavbts.models.ClusterItemData
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.tools.IMapStateStored
import org.alberto97.arpavbts.tools.IOperatorConfig
import org.alberto97.arpavbts.workers.DownloadWorker
import org.alberto97.arpavbts.workers.DownloadWorkerConstants
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val app: Application,
    private val btsRepo: IBtsRepository,
    private val operatorConfig: IOperatorConfig,
    private val mapStateStored: IMapStateStored
) : ViewModel() {
    companion object {
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

    private val venetoPosition = LatLng(45.6736317, 11.9941753)
    val defaultCameraPosition = CameraPosition.Builder()
        .target(venetoPosition)
        .zoom(7f)
        .build()

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

    private var lastCameraPosition: CameraPosition? = null

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
        state.set(MAPVIEW_BUNDLE_KEY, data)
    }

    fun restoreMapState(): Bundle {
        return state.get<Bundle>(MAPVIEW_BUNDLE_KEY) ?: getPersistedMapState()
    }

    private fun getPersistedMapState(): Bundle {
        val position = getLastCameraPosition()
        val camera = Bundle().apply {
            putParcelable("camera", position)
        }
        val state = Bundle().apply {
            putBundle("map_state", camera)
        }
        return state
    }

    fun setCameraPosition(cameraPosition: CameraPosition) {
        lastCameraPosition = cameraPosition
        saveMapState()
    }

    private fun getLastCameraPosition(): CameraPosition {
        if (lastCameraPosition != null)
            return lastCameraPosition!!

        return getPersistedCameraPosition() ?: defaultCameraPosition
    }

    private fun getPersistedCameraPosition(): CameraPosition? {
        val value = runBlocking { mapStateStored.getMapState() } ?: return null
        val lastPosition = LatLng(value.lat, value.lon)
        return CameraPosition.Builder()
            .target(lastPosition)
            .zoom(value.zoom)
            .build()
    }

    private fun saveMapState() {
        val target = lastCameraPosition?.target ?: return
        val zoom = lastCameraPosition?.zoom ?: return
        viewModelScope.launch {
            mapStateStored.setMapState(target.latitude, target.longitude, zoom)
        }
    }
}