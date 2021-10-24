package org.alberto97.arpavbts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.alberto97.arpavbts.adapters.GestoreAdapter
import org.alberto97.arpavbts.databinding.DialogOperatorsPrefBinding
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.viewmodels.MapViewModel

@AndroidEntryPoint
class OperatorsPrefBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: MapViewModel by activityViewModels()
    private lateinit var binding: DialogOperatorsPrefBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogOperatorsPrefBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = viewModel.getPreferredCarrier()
        val adapter = GestoreAdapter { gestore -> onGestoreClick(gestore) }
        binding.recyclerView.adapter = adapter
        adapter.submitList(list)
    }

    private fun onGestoreClick(data: GestoreAdapterItem) {
        setFragmentResult(
            MapRequestKey.PICK_OPERATOR,
            bundleOf(PickOperatorResultKey.OPERATOR to data.id)
        )
        findNavController().popBackStack()
    }
}