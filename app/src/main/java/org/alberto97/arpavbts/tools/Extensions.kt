package org.alberto97.arpavbts.tools

import android.view.LayoutInflater
import android.view.ViewGroup
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
}