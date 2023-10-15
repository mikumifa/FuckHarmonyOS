package com.example.chatdiary.ui.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatdiary.data.DiaryRepository

class DiaryViewModel(private val diaryRepository: DiaryRepository) : ViewModel() {
    fun updateMessageText(newText: String) {
        TODO("Not yet implemented")
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is diary Fragment"
    }
    val text: LiveData<String> = _text
}