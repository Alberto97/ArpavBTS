package org.alberto97.arpavbts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.serialization.UnstableDefault
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.viewmodels.StartViewModel
import org.alberto97.arpavbts.databinding.FragmentStartBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartFragment : Fragment() {

    private val viewModel: StartViewModel by viewModel()
    lateinit var binding: FragmentStartBinding

    @UnstableDefault
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater)

        binding.refresh.setOnClickListener {
            //viewModel.start()
        }

        binding.gotToMap.setOnClickListener {
            //val dir = StartFragmentDirections.actionStartToMap()
            //findNavController().navigate(R.id.actionStartToMap)
        }

        return binding.root
    }



}