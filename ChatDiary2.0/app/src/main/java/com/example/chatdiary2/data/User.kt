package com.example.chatdiary2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

data class UserVO(
    var email: String, var username: String, var id: Long, var userInfo: String
)