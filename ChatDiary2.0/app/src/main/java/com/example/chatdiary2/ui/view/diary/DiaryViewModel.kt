package com.example.chatdiary2.ui.view.diary


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdiary2.data.Diary
import com.example.chatdiary2.service.DiaryService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class DiaryRequest(
    val title: String,
    val content: String,
    val timestamp: Date,
    val position: String,
    val type: String,
)

@HiltViewModel
class DiaryViewModel @Inject constructor(private val diaryService: DiaryService) :
    ViewModel() {
    fun addDiary(
        type: String = "TXT", position: String, content: String, authorId: Long
    ): MutableLiveData<Boolean> {
        val currentDate = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date())
        // Create a SimpleDateFormat object with the desired pattern
        val newDiary = DiaryRequest(
            title = currentDate, content = content, timestamp = Date(),
            position = position, type = type
        )
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            kotlin.runCatching {
                diaryService.insertDiary(newDiary)
            }.onSuccess {
                result.value = true
            }.onFailure {
                val error = it.message
                result.value = false
            }
        }
        return result
    }

    fun getDiariesFlow(): MutableLiveData<List<Diary>> {

        val resultLiveData = MutableLiveData<List<Diary>>()
        viewModelScope.launch {
            flow {
                emit(diaryService.getDiaries())
            }.collect {
                val sortedData = it.data?.sortedByDescending { data -> data.timestamp }
                resultLiveData.value = sortedData
            }
        }
        return resultLiveData;
    }

    fun searchDiariesByKeywordFlow(userId: Long, keyword: String): MutableLiveData<List<Diary>> {
        val resultLiveData = MutableLiveData<List<Diary>>()
        viewModelScope.launch {
            flow {
                emit(diaryService.getDiaries())
            }.collect {
                val filteredDiaries = it.data?.filter { diary ->
                    diary.content.contains(keyword, ignoreCase = true)
                }
                val sortedData =
                    filteredDiaries?.sortedByDescending { data -> data.timestamp }
                resultLiveData.value = sortedData
            }
        }
        return resultLiveData
    }

}