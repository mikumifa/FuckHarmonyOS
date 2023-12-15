package com.example.chatdiary.data

data class UserVO(
    var email: String,
    var username: String,
    var id: Long,
    var userInfo: String?,
    var avatarUrl: String?
)