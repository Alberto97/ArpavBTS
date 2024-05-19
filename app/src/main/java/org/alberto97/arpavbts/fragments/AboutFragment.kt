package org.alberto97.arpavbts.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.viewmodels.AboutViewModel

@AndroidEntryPoint
class AboutFragment : PreferenceFragmentCompat() {

    private val viewModel: AboutViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.about, rootKey)

        val version = preferenceScreen.findPreference<Preference>("version")
        version?.summary = viewModel.appVersion

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbVersion = preferenceScreen.findPreference<Preference>("db_version")
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dbVersion.collectLatest {
                dbVersion?.summary = it
            }
        }
    }
}