package org.alberto97.arpavbts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.adapters.OperatorAdapter
import org.alberto97.arpavbts.databinding.FragmentOperatorsBinding
import org.alberto97.arpavbts.viewmodels.OperatorsViewModel

@AndroidEntryPoint
class OperatorsFragment : Fragment() {

    private lateinit var binding: FragmentOperatorsBinding

    private val viewModel: OperatorsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOperatorsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setupWithNavController(findNavController())
        setupSearch()

        val adapter = OperatorAdapter { operator ->
            setFragmentResult(
                MapRequestKey.PICK_OPERATOR,
                bundleOf(PickOperatorResultKey.OPERATOR to operator.id)
            )

            findNavController().popBackStack()
        }
        binding.list.adapter = adapter

        viewModel.operators.observe(viewLifecycleOwner) { gestori ->
            adapter.submitList(gestori)
        }
    }

    private fun setupSearch() {
        val searchItem = binding.toolbar.menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Noop
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filter(newText ?: "")
                return true
            }
        })
    }
}
