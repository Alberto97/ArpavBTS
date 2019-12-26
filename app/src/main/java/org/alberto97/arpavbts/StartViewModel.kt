package org.alberto97.arpavbts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.UnstableDefault

class StartViewModel(private val repo: IBtsRepository) : ViewModel() {

    @UnstableDefault
    fun start() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.update()
            }
        }
    }
}