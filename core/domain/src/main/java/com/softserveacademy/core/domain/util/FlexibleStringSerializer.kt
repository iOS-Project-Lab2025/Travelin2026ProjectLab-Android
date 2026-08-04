package com.softserveacademy.core.domain.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive

/**
 * A custom serializer that accepts both String and numeric values from JSON
 * and converts them into a Kotlin String.
 *
 * This is useful when an API returns the same field with different types.
 *
 * Examples:
 *
 * JSON:
 * {
 *   "id": "hotel_1"
 * }
 * Result:
 * id = "hotel_1"
 *
 * JSON:
 * {
 *   "id": 1
 * }
 * Result:
 * id = "1"
 *
 * JSON:
 * {
 *   "id": 12345
 * }
 * Result:
 * id = "12345"
 *
 * Without this serializer, Kotlin Serialization would throw a
 * JsonDecodingException when a numeric value is received for a property
 * declared as String.
 */
object FlexibleStringSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("FlexibleString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        return if (decoder is JsonDecoder) {
            val element = decoder.decodeJsonElement()
            if (element is JsonPrimitive) {
                element.content
            } else {
                element.toString()
            }
        } else {
            decoder.decodeString()
        }
    }
}
