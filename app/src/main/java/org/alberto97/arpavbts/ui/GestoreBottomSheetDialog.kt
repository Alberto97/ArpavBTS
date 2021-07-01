package org.alberto97.arpavbts.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.work.ExperimentalExpeditedWork
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.alberto97.arpavbts.adapters.GestoreAdapter
import org.alberto97.arpavbts.databinding.DialogGestoreBinding
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.viewmodels.MapViewModel

@AndroidEntryPoint
@ExperimentalExpeditedWork
class GestoreBottomSheetDialog : BottomSheetDialogFragment() {

    private val viewModel: MapViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogGestoreBinding.inflate(inflater, container, false)

        val list = viewModel.getPreferredCarrier()
        val adapter = GestoreAdapter { gestore -> onGestoreClick(gestore) }
        binding.recyclerView.adapter = adapter
        adapter.submitList(list)

        return binding.root
    }

    private fun onGestoreClick(data: GestoreAdapterItem) {
        viewModel.getBtsByCarrier(data.id)
        this.dismiss()
    }
}