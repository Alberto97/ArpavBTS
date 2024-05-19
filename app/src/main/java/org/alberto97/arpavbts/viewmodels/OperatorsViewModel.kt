package org.alberto97.arpavbts.viewmodels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import org.alberto97.arpavbts.repositories.IOperatorRepository
import javax.inject.Inject

@HiltViewModel
class OperatorsViewModel @Inject constructor(private val operatorRepository: IOperatorRepository) : ViewModel() {

    private val filter = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val operators = filter.flatMapLatest { filter ->
        operatorRepository.getOperators(filter)
    }

    fun filter(value: String) {
        filter.value = value
    }
}