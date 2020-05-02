package org.alberto97.arpavbts.viewmodels

import android.app.Application
import android.text.format.DateUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.alberto97.arpavbts.BuildConfig
import org.alberto97.arpavbts.db.IBtsRepository

class AboutViewModel(app: Application, btsRepo: IBtsRepository) : AndroidViewModel(app) {

    val appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    val dbVersion = MutableLiveData<String>()

    init {
        val options = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_YEAR
        dbVersion.value = DateUtils.formatDateTime(app, btsRepo.getLastDbUpdate(), options)
    }
}