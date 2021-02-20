package org.alberto97.arpavbts.viewmodels

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.models.GestoreConfigModel
import org.alberto97.arpavbts.repositories.IGestoreRepository
import javax.inject.Inject

@HiltViewModel
class GestoreViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: IGestoreRepository
): ViewModel() {

    fun getPreferred(): List<GestoreAdapterItem> {
        val all = GestoreAdapterItem(
            Color.parseColor("#EEEEEE"),
            context.getString(R.string.provider_all),
            null
        )

        val list = repository.getPreferred().map { mapItem(it) }.toMutableList()
        list.add(0, all)

        return list
    }

    private fun mapItem(data: GestoreConfigModel): GestoreAdapterItem {
        return GestoreAdapterItem(data.color, data.label, data.rawName)
    }
}