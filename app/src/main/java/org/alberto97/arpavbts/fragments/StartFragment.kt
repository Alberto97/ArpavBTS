package org.alberto97.arpavbts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.serialization.UnstableDefault
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.StartViewModel
import org.alberto97.arpavbts.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this)[StartViewModel::class.java]
    }
    lateinit var binding: FragmentStartBinding

    @UnstableDefault
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater)

        binding.refresh.setOnClickListener {
            viewModel.start()
        }

        binding.gotToMap.setOnClickListener {
            //val dir = StartFragmentDirections.actionStartToMap()
            findNavController().navigate(R.id.actionStartToMap)
        }

        return binding.root
    }



}