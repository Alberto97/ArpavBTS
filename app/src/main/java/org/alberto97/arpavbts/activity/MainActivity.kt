package org.alberto97.arpavbts.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.alberto97.arpavbts.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}