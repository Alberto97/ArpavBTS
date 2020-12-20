package org.alberto97.arpavbts.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.alberto97.arpavbts.adapters.GestoreAdapter
import org.alberto97.arpavbts.databinding.DialogGestoreBinding
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.viewmodels.GestoreViewModel

const val SHEET_SELECTED_PROVIDER = "Provider"

@AndroidEntryPoint
class GestoreBottomSheetDialog : BottomSheetDialogFragment() {

    private val viewModel: GestoreViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogGestoreBinding.inflate(inflater, container, false)

        val list = viewModel.getPreferred()
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
}