package com.example.chatdiary2.service

import com.example.chatdiary2.data.Diary
import com.example.chatdiary2.ui.view.diary.DiaryRequest
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DiaryService {
    @GET("/v1/diaries")
    suspend  fun getDiaries(): CommonResponse<List<Diary>>

    @POST("/v1/diary")
    suspend  fun insertDiary(@Body newDiary: DiaryRequest)
}