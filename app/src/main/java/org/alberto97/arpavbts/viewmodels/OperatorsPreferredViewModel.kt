package org.alberto97.arpavbts.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.tools.IOperatorConfig
import javax.inject.Inject

@HiltViewModel
class OperatorsPreferredViewModel @Inject constructor(config: IOperatorConfig) : ViewModel() {

    val operators = config.getPreferred().map { operator ->
        GestoreAdapterItem(operator.color, operator.label, operator.rawName)
    }
}