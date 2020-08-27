package org.alberto97.arpavbts.fragments

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.viewmodels.AboutViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("unused")
class AboutFragment : PreferenceFragmentCompat() {

    private val viewModel: AboutViewModel by viewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.about, rootKey)

        val version = preferenceScreen.findPreference<Preference>("version")
        version?.summary = viewModel.appVersion

        val dbVersion = preferenceScreen.findPreference<Preference>("db_version")
        viewModel.dbVersion.observe(this, {
            dbVersion?.summary = it
        })
    }
}