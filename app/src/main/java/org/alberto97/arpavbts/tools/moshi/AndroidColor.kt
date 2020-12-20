package org.alberto97.arpavbts.tools.moshi

import android.graphics.Color
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson


@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class AndroidColor

@Suppress("unused")
internal class AndroidColorAdapter {
    @ToJson
    fun toJson(@AndroidColor color: Int): String {
        return String.format("#%06x", color)
    }

    @FromJson
    @AndroidColor
    fun fromJson(color: String): Int {
        return Color.parseColor(color)
    }
}