package com.example.chatdiary2.service

import com.example.chatdiary2.data.Diary
import com.example.chatdiary2.ui.view.diary.DiaryRequest
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface DiaryService {
    @GET("/v1/diaries")
    suspend fun getDiaries(): CommonResponse<List<Diary>>

    @POST("/v1/diary")
    suspend fun insertDiary(@Body newDiary: DiaryRequest)

    @Multipart
    @POST("/v1/image")
    suspend fun uploadImage(
        @Part("type") type: String,
        @Part("position") position: String,
        @Part("content") content: String,
        @Part image: List<MultipartBody.Part>
    );

}