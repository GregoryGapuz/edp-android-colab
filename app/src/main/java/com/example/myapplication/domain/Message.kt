package com.example.myapplication.domain

data class Message(
    val id: String,
    val sender: String,
    val text: String,
    val createdAt: Long
)
