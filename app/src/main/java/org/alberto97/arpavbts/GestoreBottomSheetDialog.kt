package org.alberto97.arpavbts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.alberto97.arpavbts.adapters.GestoreAdapter
import org.alberto97.arpavbts.databinding.DialogGestoreBinding
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.koin.android.ext.android.inject

class GestoreBottomSheetDialog : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<DialogGestoreBinding>(
            inflater, R.layout.dialog_gestore, container, false)

        val utils : IGestoriUtils by inject()
        val list = arrayListOf(
            GestoreAdapterItem(utils.getColorForImage(all), "Tutti", all),
            GestoreAdapterItem(utils.getColorForImage(iliad), "Iliad", iliad),
            GestoreAdapterItem(utils.getColorForImage(tim), "TIM", tim),
            GestoreAdapterItem(utils.getColorForImage(vodafone), "Vodafone", vodafone),
            GestoreAdapterItem(utils.getColorForImage(windTre), "Wind Tre", windTre)
            //GestoreAdapterItem(utils.getColorForImage(others), "Altri", others)
        )

        val rec = binding.recyclerView
        rec.adapter = GestoreAdapter(list) {
            (activity as GestoreResult).onGestoreResult(it.id)
            this.dismiss()
        }

        return binding.root
    }
}