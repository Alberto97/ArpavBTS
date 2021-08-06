package org.alberto97.arpavbts.viewmodels

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.*
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.db.Bts
import org.alberto97.arpavbts.db.IBtsRepository
import org.alberto97.arpavbts.models.BTSDetailsAdapterItem
import org.alberto97.arpavbts.models.ClusterItemData
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.models.GestoreConfigModel
import org.alberto97.arpavbts.repositories.IGestoreRepository
import org.alberto97.arpavbts.tools.IGestoriUtils
import org.alberto97.arpavbts.workers.DownloadWorker
import org.alberto97.arpavbts.workers.DownloadWorkerConstants
import javax.inject.Inject

@HiltViewModel
@ExperimentalExpeditedWork
class MapViewModel @Inject constructor(
    private val app: Application,
    private val btsRepo: IBtsRepository,
    private val gestoriUtils: IGestoriUtils,
    private val gestoreRepo: IGestoreRepository
) : ViewModel() {

    private val _carrierInput = MutableLiveData<String?>()
    val btsList: LiveData<List<ClusterItemData>> = _carrierInput.switchMap { carrier ->
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

    fun getBtsByCarrier(id: String?) {
        _carrierInput.value = id
    }

    fun setBtsData(data: Bts) {
        btsDataTitle.value = data.nome

        val tempList = arrayListOf(
            BTSDetailsAdapterItem(R.drawable.ic_code_black_24dp, data.codice),
            BTSDetailsAdapterItem(R.drawable.ic_person_black_24dp, data.gestore),
            BTSDetailsAdapterItem(R.drawable.ic_terrain_black_24dp, "${data.quotaSlm} m"),
            BTSDetailsAdapterItem(R.drawable.ic_place_black_24dp, data.indirizzo),
            BTSDetailsAdapterItem(R.drawable.ic_find_in_page_black_24dp, data.postazione),
            BTSDetailsAdapterItem(
                R.drawable.ic_settings_input_antenna_black_24dp,
                data.pontiRadio
            )
        )

        // Remove missing info
        val list = tempList.filter { !it.text.isNullOrEmpty() && it.text != "NO DATA" }
        btsData.value = list
    }

    fun setGestoreData(data: List<ClusterItemData>) {
        val list = data.map {
            GestoreAdapterItem(
                gestoriUtils.getColor(it.data.gestore),
                it.data.nome,
                it.data.idImpianto.toString()
            )
        }
        gestoreData.value = list
    }

    fun getPreferredCarrier(): List<GestoreAdapterItem> {
        val all = GestoreAdapterItem(
            Color.parseColor("#EEEEEE"),
            app.getString(R.string.provider_all),
            null
        )

        val list = gestoreRepo.getPreferred().map { mapCarrier(it) }.toMutableList()
        list.add(0, all)

        return list
    }

    private fun mapCarrier(data: GestoreConfigModel): GestoreAdapterItem {
        return GestoreAdapterItem(data.color, data.label, data.rawName)
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
}