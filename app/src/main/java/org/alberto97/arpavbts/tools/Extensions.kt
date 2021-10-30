package org.alberto97.arpavbts.tools

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.core.graphics.ColorUtils

object Extensions {
    fun Int.toHue(): Float {
        val hsvValues = FloatArray(3)
        ColorUtils.colorToHSL(this, hsvValues)
        return hsvValues[0]
    }

    fun ViewGroup.getInflater(): LayoutInflater {
        return LayoutInflater.from(this.context)
    }

    fun Configuration.isNightModeOn(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.isNightModeActive
        } else {
            val currentNightMode = this.uiMode and Configuration.UI_MODE_NIGHT_MASK
            currentNightMode == Configuration.UI_MODE_NIGHT_YES
        }
    }

    fun Resources.readRawTextFile(@RawRes id: Int): String =
        openRawResource(id).bufferedReader().use { it.readText() }

    fun TextView.setStartDrawable(@DrawableRes value: Int) {
        return setCompoundDrawablesRelativeWithIntrinsicBounds(value, 0, 0, 0)
    }
}