package org.alberto97.arpavbts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.UnstableDefault

class StartViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: BtsRepository = BtsRepository.instance(app)

    @UnstableDefault
    fun start() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.update()
            }
        }
    }
}