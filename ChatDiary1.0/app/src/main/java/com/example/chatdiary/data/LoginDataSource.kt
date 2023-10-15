package com.example.chatdiary.data

import com.example.chatdiary.data.model.LoggedInUser
import HttpUtils
import com.example.chatdiary.R
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val httpUtils = HttpUtils()
    private val serverUrl = "http://10.0.2.2:8080"

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {

            // Create a JSON request body with the username and password
            val jsonBody = """
                {
                    "username": "$username",
                    "password": "$password"
                }
            """.trimIndent()

            val response = httpUtils.doPost("$serverUrl/v1/session", jsonBody)

            return if (response != null) {
                val loggedInUser = LoggedInUser(username, username)
                Result.Success(loggedInUser)
            } else {
                Result.Error(IOException("Authentication failed"))
            }
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout(): Result<String> {
        try {
            val response = httpUtils.doDelete("$serverUrl/v1/session", "")

            return if (response != null) {
                Result.Success("success")
            } else {
                Result.Error(IOException("Authentication failed"))
            }
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }
}