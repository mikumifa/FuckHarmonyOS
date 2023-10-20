package com.example.chatdiary2.service

import com.example.chatdiary2.data.UserVO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT


interface UserService {
    @POST("/v1/session")
    suspend fun login(@Body request: LoginRequest?): CommonResponse<UserVO?>

    @POST("/v1/user")
    suspend fun register(@Body request: RegisterRequest?): CommonResponse<String?>

    @DELETE("/v1/session")
    suspend fun logout(): CommonResponse<Void?>

    @GET("/v1/user")
    suspend fun userInfo(): CommonResponse<Void?>

    @PUT("/v1/user")
    suspend fun editInfo(@Body request: EditUserInfoRequest?): CommonResponse<Void?>
}

data class EditUserInfoRequest(
    val username: String, val email: String
)

data class RegisterRequest(
    val username: String, val email: String, val password: String
)


data class LoginRequest(
    val email: String, val password: String
)
