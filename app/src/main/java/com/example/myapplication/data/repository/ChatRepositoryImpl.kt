package com.example.myapplication.data.repository

import com.example.myapplication.core.AppResult
import com.example.myapplication.data.local.MessageDao
import com.example.myapplication.data.network.ChatApiService
import com.example.myapplication.data.network.dto.NewMessageDto
import com.example.myapplication.data.network.dto.toDomain
import com.example.myapplication.data.network.dto.toEntity
import com.example.myapplication.domain.ChatRepository
import com.example.myapplication.domain.Message
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ChatRepositoryImpl(
    private val api: ChatApiService,
    private val dao: MessageDao
) : ChatRepository {

    override suspend fun getMessages(): AppResult<List<Message>> {
        val result = safeCall { api.getMessages().toDomain() }
        if (result is AppResult.Success) {
            dao.insertAll(result.data.map { it.toEntity() })
            return result
        }
        
        val saved = dao.getAll().map { it.toDomain() }
        return if (saved.isNotEmpty()) AppResult.Success(saved) else result
    }

    override suspend fun sendMessage(sender: String, text: String): AppResult<Unit> = safeCall {
        val dto = NewMessageDto(sender, text, System.currentTimeMillis())
        api.sendMessage(dto)
        Unit
    }

    override suspend fun clearOldMessages(): AppResult<Unit> = safeCall {
        val messages = api.getMessages(order = "asc") // oldest first
        messages.take(50).forEach { 
            it.id?.let { id -> api.deleteMessage(id) }
        }
        Unit
    }

    override suspend fun deleteMessage(id: String): AppResult<Unit> = safeCall {
        api.deleteMessage(id)
        Unit
    }

    private inline fun <T> safeCall(block: () -> T): AppResult<T> =
        try {
            AppResult.Success(block())
        } catch (e: retrofit2.HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            android.util.Log.e("ChatRepo", "HTTP Error ${e.code()}: $errorBody")
            AppResult.Failure.Unknown(errorBody ?: "HTTP ${e.code()}")
        } catch (e: UnknownHostException) {
            android.util.Log.e("ChatRepo", "No internet", e)
            AppResult.Failure.NoInternet
        } catch (e: SocketTimeoutException) {
            android.util.Log.e("ChatRepo", "Timeout", e)
            AppResult.Failure.Timeout
        } catch (e: IOException) {
            android.util.Log.e("ChatRepo", "IO Error", e)
            AppResult.Failure.NoInternet
        } catch (e: Exception) {
            android.util.Log.e("ChatRepo", "Unknown Error: ${e.message}", e)
            AppResult.Failure.Unknown(e.message)
        }
}
