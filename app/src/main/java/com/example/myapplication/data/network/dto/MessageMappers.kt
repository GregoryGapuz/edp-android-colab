package com.example.myapplication.data.network.dto

import com.example.myapplication.domain.Message
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.contentOrNull
import java.text.SimpleDateFormat
import java.util.*

fun MessageDto.toDomain(): Message {
    val dateValue = when {
        createdAt is JsonPrimitive && createdAt.isString -> {
            val dateStr = createdAt.contentOrNull
            if (dateStr != null) {
                try {
                    // Try parsing common ISO format found in MockAPI
                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                    sdf.timeZone = TimeZone.getTimeZone("UTC")
                    sdf.parse(dateStr)?.time ?: 0L
                } catch (e: Exception) {
                    0L
                }
            } else 0L
        }
        createdAt is JsonPrimitive -> createdAt.longOrNull ?: 0L
        else -> 0L
    }

    return Message(
        id = id ?: "",
        sender = sender ?: "Unknown",
        text = text ?: "",
        createdAt = dateValue
    )
}

fun List<MessageDto>.toDomain(): List<Message> =
    map { it.toDomain() }

fun Message.toEntity(): com.example.myapplication.data.local.MessageEntity =
    com.example.myapplication.data.local.MessageEntity(
        id = id,
        sender = sender,
        text = text,
        createdAt = createdAt
    )

fun com.example.myapplication.data.local.MessageEntity.toDomain(): Message = Message(
    id = id,
    sender = sender,
    text = text,
    createdAt = createdAt
)
