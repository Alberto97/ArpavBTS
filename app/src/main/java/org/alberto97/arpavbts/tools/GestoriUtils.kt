package org.alberto97.arpavbts.tools

import android.graphics.Color
import javax.inject.Inject
import javax.inject.Singleton

interface IGestoriUtils {
    fun getColor(gestore: String): Int
}

@Singleton
class GestoriUtils @Inject constructor() : IGestoriUtils {

    override fun getColor(gestore: String): Int {
        val color = carrierColor[gestore] ?: otherColor
        return Color.parseColor(color)
    }
}