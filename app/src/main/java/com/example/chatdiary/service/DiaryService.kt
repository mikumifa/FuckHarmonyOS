package com.example.chatdiary.service

import com.example.chatdiary.data.Diary
import com.example.chatdiary.ui.view.main.diary.DiaryRequest
import com.example.chatdiary.ui.view.main.diary.DayDiaryVo
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

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

    @GET("/v1/gen/diaryDateList")
    suspend fun getDiaryGenList(
        @Query("number") number: Long,
    ): CommonResponse<List<DayDiaryVo>>;
}