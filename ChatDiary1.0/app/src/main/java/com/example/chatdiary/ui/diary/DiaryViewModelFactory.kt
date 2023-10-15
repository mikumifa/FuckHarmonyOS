package com.example.chatdiary.ui.diary;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatdiary.data.DiaryDataSource
import com.example.chatdiary.data.DiaryRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class DiaryViewModelFactory : ViewModelProvider.Factory {

@Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
        return DiaryViewModel(
        diaryRepository = DiaryRepository(
        dataSource = DiaryDataSource()
        )
        ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
        }
        }