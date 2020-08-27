package org.alberto97.arpavbts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import org.alberto97.arpavbts.databinding.FragmentAboutWrapperBinding

class AboutFragmentWrapper : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAboutWrapperBinding.inflate(inflater)
        binding.toolbar.setupWithNavController(findNavController())

        return binding.root
    }
}