package com.example.chatdiary2.service

import CommonResponse
import EditUserInfoRequest
import LoginRequest
import RegisterRequest
import UserVO
import com.example.chatdiary2.config.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.*
import retrofit2.converter.gson.GsonConverterFactory

interface UserService {
    @POST("session")
    fun login(@Body request: LoginRequest): Call<CommonResponse<Any>>

    @POST("user")
    fun register(@Body request: RegisterRequest): Call<CommonResponse<Any>>

    @DELETE("session")
    fun logout(): Call<CommonResponse<Any>>

    @GET("user")
    fun getUserInfo(): Call<CommonResponse<UserVO>>

    @PUT("user")
    fun editUserInfo(@Body request: EditUserInfoRequest): Call<CommonResponse<Any>>

    companion object {

        private const val BASE_URL = "http://127.0.0.1:8080/"
        fun create(): UserService {
            val logger = HttpLoggingInterceptor()
            logger.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
            val client = OkHttpClient.Builder().addInterceptor(logger).build()
            return Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create())
                .build().create(UserService::class.java)
        }
    }
}
