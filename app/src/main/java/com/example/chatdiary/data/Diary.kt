package com.example.chatdiary.data


data class Diary(
    val id: Long,
    val title: String,
    val content: String,
    val timestamp: String,
    val position: String,
    val type: String,
    val images: List<String>
)


