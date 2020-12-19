package org.alberto97.arpavbts.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.adapters.GestoreAdapter
import org.alberto97.arpavbts.databinding.DialogGestoreBinding
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.tools.*

const val SHEET_SELECTED_PROVIDER = "Provider"
class GestoreBottomSheetDialog : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogGestoreBinding.inflate(inflater, container, false)

        val list = arrayListOf(
            getItem(getString(R.string.provider_all), null),
            getItem(getString(R.string.provider_tim), timName),
            getItem(getString(R.string.provider_vodafone), vodafoneName),
            getItem(getString(R.string.provider_windtre), windTreName),
            getItem(getString(R.string.provider_iliad), iliadName)
        )

        val adapter = GestoreAdapter { gestore -> onGestoreClick(gestore) }
        binding.recyclerView.adapter = adapter
        adapter.submitList(list)

        return binding.root
    }

    private fun onGestoreClick(data: GestoreAdapterItem) {
        val intent = Intent()
        intent.putExtra(SHEET_SELECTED_PROVIDER, data.id)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
        this.dismiss()
    }

    private fun getItem(name: String, id: String?): GestoreAdapterItem {
        val colorStr = carrierColor[id] ?: allColor
        val color = Color.parseColor(colorStr)

        return GestoreAdapterItem(color, name, id)
    }
}