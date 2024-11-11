package com.westerra.release.gson.adapter

import com.backbase.android.client.paymentorderclient2.model.Schedule.Every
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/**
 * Adapter for serializing [Every] to JSON.
 * Backend API expects this format in some UseCases.
 */
class BackBaseEveryAdapter : JsonSerializer<Every?> {
    override fun serialize(
        src: Every?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?,
    ): JsonElement? {
        src ?: return null
        val result = when (src) {
            Every._1 -> "1"
            Every._2 -> "2"
        }
        return JsonPrimitive(result)
    }
}
