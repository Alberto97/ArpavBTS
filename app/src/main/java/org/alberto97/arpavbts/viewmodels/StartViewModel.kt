package org.alberto97.arpavbts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.UnstableDefault
import org.alberto97.arpavbts.repositories.IBtsRepository

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