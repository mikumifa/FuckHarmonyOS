package com.example.chatdiary2.ui.view.diary


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdiary2.data.Diary
import com.example.chatdiary2.service.DiaryService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class DiaryRequest(
    val title: String,
    val content: String,
    val timestamp: String,

    val position: String,
    val type: String,
)

@HiltViewModel
class DiaryViewModel @Inject constructor(private val diaryService: DiaryService) : ViewModel() {

    fun addDiary(
        type: String = "TXT", position: String, content: String, authorId: Long
    ): MutableLiveData<Boolean> {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.US)
        val currentDate = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date())
        // Create a SimpleDateFormat object with the desired pattern
        val newDiary = DiaryRequest(
            title = currentDate,
            content = content,
            timestamp = dateFormat.format(Date()),
            position = position,
            type = type
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

    fun searchDiariesByKeywordFlow(keyword: String): MutableLiveData<List<Diary>> {
        val resultLiveData = MutableLiveData<List<Diary>>()
        viewModelScope.launch {
            flow {
                emit(diaryService.getDiaries())
            }.collect {
                val filteredDiaries = it.data?.filter { diary ->
                    diary.content.contains(keyword, ignoreCase = true)
                }
                val sortedData = filteredDiaries?.sortedByDescending { data -> data.timestamp }
                resultLiveData.value = sortedData
            }
        }
        return resultLiveData
    }

    fun searchDiariesByDateFlow(targetDate: LocalDate): MutableLiveData<List<Diary>> {
        val resultLiveData = MutableLiveData<List<Diary>>()
        viewModelScope.launch {
            flow {
                emit(diaryService.getDiaries())
            }.collect {
                val filteredDiaries = it.data?.filter { diary ->
                    if (diary.timestamp != null) {
                        val diaryDate =
                            diary.timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        diaryDate == targetDate
                    } else {
                        false
                    }


                }
                val sortedData = filteredDiaries?.sortedByDescending { data -> data.timestamp }
                resultLiveData.value = sortedData
            }
        }
        return resultLiveData
    }


}