package com.example.chatdiary2.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date


data class Diary(
    val id: Long,
    val title: String,
    val content: String,
    val timestamp: String,
    val position: String,
    val type: String,
    val images: List<String>
)


