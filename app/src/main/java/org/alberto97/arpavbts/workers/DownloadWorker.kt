package org.alberto97.arpavbts.workers

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.db.IBtsRepository

object DownloadWorkerConstants {
    const val FORCE_UPDATE = "force_update"
}

@HiltWorker
class DownloadWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val btsRepo: IBtsRepository,
):
    CoroutineWorker(appContext, workerParams) {

    private val notificationManager = NotificationManagerCompat.from(appContext)
    private val channelId = "bts_updates"

    override suspend fun doWork(): Result {
        val forceUpdate = inputData.getBoolean(DownloadWorkerConstants.FORCE_UPDATE, false)

        try {
            if (forceUpdate) {
                btsRepo.updateBts()
            } else {
                btsRepo.updateBtsIfOldOrEmpty()
            }
        } catch (e: Exception) {
            return Result.failure()
        }

        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return createForegroundInfo()
    }

    /**
     * Create ForegroundInfo required to run a Worker in a foreground service.
     */
    private fun createForegroundInfo(): ForegroundInfo {
        // Use a different id for each Notification.
        val notificationId = 1
        return ForegroundInfo(notificationId, createNotification())
    }

    /**
     * Create the notification and required channel (O+) for running work
     * in a foreground service.
     */
    private fun createNotification(): Notification {

        val title = appContext.getString(R.string.bts_update_notification_title)
        val builder = NotificationCompat.Builder(appContext, channelId)
            .setContentTitle(title)
            .setTicker(title)
            .setSmallIcon(R.drawable.ic_bts_white)
            .setOngoing(true)
            .setProgress(100, 0, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = appContext.getString(R.string.bts_update_notification_channel)
            createNotificationChannel(channelId, channelName).also {
                builder.setChannelId(it.id)
            }
        }

        return builder.build()
    }

    /**
     * Create the required notification channel for O+ devices.
     */
    @Suppress("SameParameterValue")
    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        id: String,
        name: String
    ): NotificationChannel {
        return NotificationChannel(
            id, name, NotificationManager.IMPORTANCE_LOW
        ).also { channel ->
            notificationManager.createNotificationChannel(channel)
        }
    }
}