package org.alberto97.arpavbts.viewmodels

import android.content.Context
import android.text.format.DateUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.alberto97.arpavbts.BuildConfig
import org.alberto97.arpavbts.db.IBtsRepository

class AboutViewModel @ViewModelInject constructor(@ApplicationContext context: Context, btsRepo: IBtsRepository) : ViewModel() {

    val appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    val dbVersion = MutableLiveData<String>()

    init {
        val options = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_YEAR
        dbVersion.value = DateUtils.formatDateTime(context, btsRepo.getLastDbUpdate(), options)
    }
}