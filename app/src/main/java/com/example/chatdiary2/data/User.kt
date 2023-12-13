package com.example.chatdiary2.data

data class UserVO(
    var email: String,
    var username: String,
    var id: Long,
    var userInfo: String?,
    var avatarUrl: String?
)