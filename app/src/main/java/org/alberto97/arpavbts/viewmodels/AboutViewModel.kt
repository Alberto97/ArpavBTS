package org.alberto97.arpavbts.viewmodels

import android.app.Application
import android.text.format.DateUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.alberto97.arpavbts.BuildConfig
import org.alberto97.arpavbts.db.IBtsRepository
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(app: Application, btsRepo: IBtsRepository) : ViewModel() {

    val appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    val dbVersion = MutableLiveData<String>()

    init {
        val options = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_YEAR
        dbVersion.value = DateUtils.formatDateTime(app, btsRepo.getLastDbUpdate(), options)
    }
}