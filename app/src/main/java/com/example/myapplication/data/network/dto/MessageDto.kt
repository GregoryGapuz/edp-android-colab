package com.example.myapplication.data.network.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class MessageDto(
    val id: String? = null,
    val sender: String? = null,
    val text: String? = null,
    val createdAt: JsonElement? = null
)

@Serializable
data class NewMessageDto(
    val sender: String,
    val text: String,
    val createdAt: Long
)
