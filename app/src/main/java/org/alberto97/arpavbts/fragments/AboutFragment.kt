package org.alberto97.arpavbts.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.preference.Preference
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.viewmodels.AboutViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : ToolbarPreferenceFragment() {

    private val viewModel: AboutViewModel by viewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.about, rootKey)

        val version = preferenceScreen.findPreference<Preference>("version")
        version?.summary = viewModel.appVersion

        val dbVersion = preferenceScreen.findPreference<Preference>("db_version")
        viewModel.dbVersion.observe(this, Observer {
            dbVersion?.summary = it
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getToolbar()?.title = getString(R.string.about_title)
        getToolbar()?.setDisplayHomeAsUpEnabled(true)
    }
}