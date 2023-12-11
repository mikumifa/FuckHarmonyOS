package com.example.chatdiary2.ui.view.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
public fun SettingStyledText(
    text: String, color: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Text(
        text = text, style = TextStyle(
            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color
        )
    )
}