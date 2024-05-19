package org.alberto97.arpavbts.viewmodels

import android.app.Application
import android.text.format.DateUtils
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.alberto97.arpavbts.BuildConfig
import org.alberto97.arpavbts.db.IBtsRepository
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(app: Application, btsRepo: IBtsRepository) : ViewModel() {

    val appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

    private val _dbVersion = MutableStateFlow("")
    val dbVersion = _dbVersion.asStateFlow()

    init {
        val options = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_YEAR
        _dbVersion.value = DateUtils.formatDateTime(app, btsRepo.getLastDbUpdate(), options)
    }
}