package org.alberto97.arpavbts.models

import kotlinx.serialization.Serializable
import org.alberto97.arpavbts.tools.serializers.AndroidColorSerializer

@Serializable
class GestoreConfigModel(
    val label: String,
    val rawName: String,
    @Serializable(with = AndroidColorSerializer::class)
    val color: Int,
    val preferred: Boolean = false
)