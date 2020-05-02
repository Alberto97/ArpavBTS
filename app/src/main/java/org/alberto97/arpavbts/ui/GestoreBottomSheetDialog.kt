package org.alberto97.arpavbts.ui

import android.app.Activity
import android.content.Intent
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

const val SHEET_SELECTED_GESTORE_ID = "GestoreId"
class GestoreBottomSheetDialog : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<DialogGestoreBinding>(
            inflater,
            R.layout.dialog_gestore, container, false)

        val list = arrayListOf(
            getItem(getString(R.string.provider_all), all),
            getItem(getString(R.string.provider_tim), tim),
            getItem(getString(R.string.provider_vodafone), vodafone),
            getItem(getString(R.string.provider_windtre), windTre),
            getItem(getString(R.string.provider_iliad), iliad)
        )

        val adapter = GestoreAdapter { gestore -> onGestoreClick(gestore) }
        binding.recyclerView.adapter = adapter
        adapter.submitList(list)

        return binding.root
    }

    private fun onGestoreClick(data: GestoreAdapterItem) {
        val intent = Intent()
        intent.putExtra(SHEET_SELECTED_GESTORE_ID, data.id)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
        this.dismiss()
    }

    private fun getItem(name: String, id: String): GestoreAdapterItem {
        val colorStr = carrierColor[id] ?: allColor
        val color = Color.parseColor(colorStr)

        return GestoreAdapterItem(color, name, id)
    }
}