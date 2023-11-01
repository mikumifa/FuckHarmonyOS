package com.example.chatdiary2.service

import com.example.chatdiary2.ui.view.chat.ChatRequest
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