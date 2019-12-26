package org.alberto97.arpavbts.tools

import androidx.core.graphics.ColorUtils

object Extensions {
    fun Int.toHue(): Float {
        val hsvValues = FloatArray(3)
        ColorUtils.colorToHSL(this, hsvValues)
        return hsvValues[0]
    }
}