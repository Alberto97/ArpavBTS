package org.alberto97.arpavbts.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.adapters.GestoreAdapter
import org.alberto97.arpavbts.databinding.DialogGestoreBinding
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.tools.*

class GestoreBottomSheetDialog : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<DialogGestoreBinding>(
            inflater,
            R.layout.dialog_gestore, container, false)

        val list = arrayListOf(
            getItem("Tutti", all),
            getItem("TIM", tim),
            getItem("Vodafone", vodafone),
            getItem("Wind Tre", windTre),
            getItem("Iliad", iliad)
            //getItem("Altri", others)
        )

        val rec = binding.recyclerView
        rec.adapter = GestoreAdapter(list) {
            (activity as GestoreResult).onGestoreResult(it.id)
            this.dismiss()
        }

        return binding.root
    }

    private fun getItem(name: String, id: String): GestoreAdapterItem {
        val colorStr = carrierColor[id] ?: allColor
        val color = Color.parseColor(colorStr)

        return GestoreAdapterItem(color, name, id)
    }
}