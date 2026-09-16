package com.example.myapplication.domain

import com.example.myapplication.core.AppResult

interface ChatRepository {
    suspend fun getMessages(): AppResult<List<Message>>
    suspend fun sendMessage(sender: String, text: String): AppResult<Unit>
    suspend fun clearOldMessages(): AppResult<Unit>
    suspend fun deleteMessage(id: String): AppResult<Unit>
}
