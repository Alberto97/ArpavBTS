package org.alberto97.arpavbts.tools.serializers

import android.graphics.Color
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


object AndroidColorSerializer : KSerializer<Int> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Int {
        val string = decoder.decodeString()
        return Color.parseColor(string)
    }

    override fun serialize(encoder: Encoder, value: Int) {
        val string = String.format("#%06x", value)
        encoder.encodeString(string)
    }
}
