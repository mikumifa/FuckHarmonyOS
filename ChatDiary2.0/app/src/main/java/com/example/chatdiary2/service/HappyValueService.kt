package com.example.chatdiary2.service

import com.example.chatdiary2.data.HappyValue
import retrofit2.http.GET
import retrofit2.http.Query

interface HappyValueService {
    @GET("/v1/happydata/list")
    suspend fun getHappyValueList(@Query("timestamp") timestamp: Long): CommonResponse<List<HappyValue>?>

}
