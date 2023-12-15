package com.example.chatdiary.service

import com.example.chatdiary.ui.view.main.chat.ChatRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class Message(
    val content: String,
    val timestamp: String,
    val isUserMe: Boolean
)

interface ChatService {
    @POST("/v1/chat")
    suspend fun putQuestion(@Body newChat: ChatRequest): CommonResponse<Boolean>

    @GET("/v1/chat")
    suspend fun getMessages(): CommonResponse<List<Message>>
}