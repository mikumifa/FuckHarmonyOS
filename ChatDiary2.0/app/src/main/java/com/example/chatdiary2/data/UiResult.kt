package com.example.chatdiary2.data

data class UiResult<T>(
    val data: T, val msg: String = "", val isOK: Boolean = false, val isSuccess: Boolean = false
)