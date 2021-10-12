package org.alberto97.arpavbts.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.AppTheme)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}