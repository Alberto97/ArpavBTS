package org.alberto97.arpavbts.tools

import android.graphics.Color

interface IGestoriUtils {
    fun getColor(gestore: String): Int
}

class GestoriUtils : IGestoriUtils {

    override fun getColor(gestore: String): Int {
        val color = carrierColor[gestore] ?: otherColor
        return Color.parseColor(color)
    }
}