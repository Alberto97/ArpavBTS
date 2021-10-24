package org.alberto97.arpavbts.viewmodels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import org.alberto97.arpavbts.repositories.IOperatorRepository
import javax.inject.Inject

@HiltViewModel
class OperatorsViewModel @Inject constructor(private val operatorRepository: IOperatorRepository) : ViewModel() {

    private val filter = MutableLiveData("")

    val operators = filter.switchMap { filter ->
        operatorRepository.getOperators(filter).asLiveData()
    }

    fun filter(value: String) {
        filter.value = value
    }
}