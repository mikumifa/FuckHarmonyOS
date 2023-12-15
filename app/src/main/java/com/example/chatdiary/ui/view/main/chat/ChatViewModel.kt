package com.example.chatdiary.ui.view.main.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdiary.service.ChatService
import com.example.chatdiary.service.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class ChatRequest(
    val content: String,
    val timestamp: String,
)

data class Result<T>(
    var Message: T,
    var isSuccess: Boolean,
    var ErrorMessage: String,
)

@HiltViewModel
class ChatViewModel @Inject constructor(private val chatService: ChatService) : ViewModel() {
    fun addChat(
        content: String
    ): MutableLiveData<Boolean> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.US)
        val newChat = ChatRequest(
            content = content,
            timestamp = dateFormat.format(Date()),
        )
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            kotlin.runCatching {
                chatService.putQuestion(newChat)
            }.onSuccess {
                result.value = true
            }.onFailure {
                Log.d("******", it.message!!)
                result.value = false
            }
        }
        return result
    }

    fun getMessageFlow(): MutableLiveData<List<Message>> {

        val resultLiveData = MutableLiveData<List<Message>>()
        viewModelScope.launch {

            flow {
                emit(chatService.getMessages())
            }.collect {
                val sortedData = it.data?.sortedByDescending { data -> data.timestamp }
                resultLiveData.value = sortedData
            }
        }
        return resultLiveData;
    }
}