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
import org.alberto97.arpavbts.models.ClusterItemData
import org.alberto97.arpavbts.tools.carrierNameById

class MapViewModel(val app: Application, private val btsRepo: IBtsRepository) : AndroidViewModel(app) {

    private val notificationManager = app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val notificationId = 1

    private val _carrierInput = MutableLiveData<String>()
    private val _btsList: LiveData<List<Bts>> = Transformations.switchMap(_carrierInput) {
        val carrier = carrierNameById[it]
        btsRepo.getBts(carrier)
    }

    val btsList: LiveData<List<ClusterItemData>> = Transformations.map(_btsList) {
        list -> list.map { ClusterItemData(it) }
    }

    init {
        viewModelScope.launch {
            val isEmpty = withContext(Dispatchers.IO) {
                btsRepo.isEmpty()
            }
            if (isEmpty)
                updateDb()
        }
    }

    fun getBtsByCarrier(id: String) {
        _carrierInput.value = id
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel("bts_updates", "Aggiornamenti", NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }

    fun updateDb() {
        createChannel()
        val notificationBuilder = NotificationCompat.Builder(app, "bts_updates")
            .setSmallIcon(R.drawable.ic_bts_white)
            .setContentTitle("Updating BTS...")
            .setOngoing(true)
            .setProgress(100, 0, true)

        val notification: Notification = notificationBuilder.build()
        notificationManager.notify(notificationId, notification)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                btsRepo.updateBts()
            }
            withContext(Dispatchers.Main) {
                notificationManager.cancel(notificationId)
            }
        }
    }
}