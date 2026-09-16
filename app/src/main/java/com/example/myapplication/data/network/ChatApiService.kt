package com.example.myapplication.data.network

import com.example.myapplication.data.network.dto.MessageDto
import com.example.myapplication.data.network.dto.NewMessageDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatApiService {
    @GET("messages")
    suspend fun getMessages(
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("order") order: String = "desc"
    ): List<MessageDto>

    @POST("messages")
    suspend fun sendMessage(@Body message: NewMessageDto): MessageDto

    @retrofit2.http.DELETE("messages/{id}")
    suspend fun deleteMessage(@retrofit2.http.Path("id") id: String): MessageDto
}
