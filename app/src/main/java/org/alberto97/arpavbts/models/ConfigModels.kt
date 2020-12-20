package org.alberto97.arpavbts.models

import com.squareup.moshi.JsonClass
import org.alberto97.arpavbts.tools.moshi.AndroidColor

@JsonClass(generateAdapter = true)
class GestoriConfigModel(val gestori: List<GestoreConfigModel>)

@JsonClass(generateAdapter = true)
class GestoreConfigModel(
    val label: String,
    val rawName: String,
    @AndroidColor val color: Int,
    val preferred: Boolean
)