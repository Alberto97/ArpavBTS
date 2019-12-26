package org.alberto97.arpavbts.tools

import android.graphics.Color

interface IGestoriUtils {
    fun getColor(gestore: String): Int
}

class GestoriUtils : IGestoriUtils {

    override fun getColor(gestore: String): Int {
        val id = carrierIdByName[gestore]
        val color = carrierColor[id] ?: otherColor
        return Color.parseColor(color)
    }
}