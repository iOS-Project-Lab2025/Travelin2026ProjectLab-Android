package com.softserveacademy.core.domain.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder

/**
 * A custom serializer that accepts both a single String and a list of Strings from JSON
 * and always returns a List<String>.
 *
 * This is useful when an API returns a single URL for "imageUrl" in some cases,
 * and a list of URLs in others.
 */
object FlexibleListSerializer : KSerializer<List<String>> {
    private val delegateSerializer = ListSerializer(String.serializer())
    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    override fun serialize(encoder: Encoder, value: List<String>) {
        encoder.encodeSerializableValue(delegateSerializer, value)
    }

    override fun deserialize(decoder: Decoder): List<String> {
        return if (decoder is JsonDecoder) {
            val element = decoder.decodeJsonElement()
            if (element is JsonArray) {
                decoder.json.decodeFromJsonElement(delegateSerializer, element)
            } else {
                listOf(element.toString().removeSurrounding("\""))
            }
        } else {
            listOf(decoder.decodeString())
        }
    }
}
