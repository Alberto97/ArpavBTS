package org.alberto97.arpavbts.viewmodels

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.db.Bts
import org.alberto97.arpavbts.db.IBtsRepository
import org.alberto97.arpavbts.models.BTSDetailsAdapterItem
import org.alberto97.arpavbts.models.ClusterItemData
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.tools.IGestoriUtils

class MapViewModel(
    val app: Application,
    private val btsRepo: IBtsRepository,
    private val gestoriUtils: IGestoriUtils) : AndroidViewModel(app) {

    private val notificationManager = app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val notificationId = 1
    private val notificationChannel = "bts_updates"

    private val _carrierInput = MutableLiveData<String>()
    private val _btsList: LiveData<List<Bts>> = Transformations.switchMap(_carrierInput) {
        btsRepo.getBts(it)
    }

    val btsList: LiveData<List<ClusterItemData>> = Transformations.map(_btsList) {
        list -> list.map { ClusterItemData(it) }
    }

    val btsDataTitle = MutableLiveData<String>()
    val btsData = MutableLiveData<List<BTSDetailsAdapterItem>>()
    val gestoreData = MutableLiveData<List<GestoreAdapterItem>>()

    init {
        viewModelScope.launch {
            updateDb()
        }
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

        // Remove missing infos
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

    private fun createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channelName = app.getString(R.string.bts_update_notification_channel)
        val channel = NotificationChannel(notificationChannel, channelName, NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }

    fun updateDb(forceUpdate: Boolean = false) {
        createChannel()
        val title = app.getString(R.string.bts_update_notification_title)
        val notificationBuilder = NotificationCompat.Builder(app, notificationChannel)
            .setSmallIcon(R.drawable.ic_bts_white)
            .setContentTitle(title)
            .setOngoing(true)
            .setProgress(100, 0, true)

        val notification: Notification = notificationBuilder.build()
        notificationManager.notify(notificationId, notification)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (forceUpdate) {
                    btsRepo.updateBts()
                } else {
                    btsRepo.updateBtsIfOldOrEmpty()
                }
            }
            withContext(Dispatchers.Main) {
                notificationManager.cancel(notificationId)
            }
        }
    }
}