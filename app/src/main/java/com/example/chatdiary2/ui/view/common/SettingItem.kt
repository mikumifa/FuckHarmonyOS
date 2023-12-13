package com.example.chatdiary2.ui.view.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatdiary2.R
import com.example.chatdiary2.ui.theme.AppTheme
import com.example.chatdiary2.ui.view.common.AppThemesList

@Composable
fun SettingItem(content: @Composable () -> Unit) {
    Spacer(modifier = Modifier.height(10.dp))
    content()
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
    )
}