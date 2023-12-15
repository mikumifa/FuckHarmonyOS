package com.example.chatdiary.service

import com.example.chatdiary.data.HappyValue
import retrofit2.http.GET
import retrofit2.http.Query

interface HappyValueService {
    @GET("/v1/happydata/list")
    suspend fun getHappyValueList(@Query("timestamp") timestamp: Long): CommonResponse<List<HappyValue>?>

}
