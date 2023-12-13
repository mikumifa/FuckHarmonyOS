package com.example.chatdiary2.ui.view.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable

fun SwitchBottom(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    onClick: (Boolean) -> Unit, // 点击事件的回调
    modifier: Modifier = Modifier,
    initState: Boolean = false
) {

    var switchState by remember {
        mutableStateOf(initState)
    }
    Row(
        modifier = modifier
            .padding(4.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .height(48.dp)
            .clickable {

            }, // 添加点击事件
        horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        icon()
        Spacer(modifier = Modifier.width(8.dp))
        text()
        Spacer(modifier = Modifier.weight(1f))
        Switch(checked = switchState, onCheckedChange = { newCheckedState ->
            switchState = newCheckedState
            onClick(newCheckedState)
        })
        Spacer(modifier = Modifier.width(16.dp))

    }
}

@Preview
@Composable
fun SwitchBottomPreview() {
    Column(modifier = Modifier) {
        SwitchBottom(icon = {
            Icon(Icons.Filled.Lock, contentDescription = "edit sentiment")
        },
            text = { SettingStyledText(text = "启动设备锁") },
            modifier = Modifier.fillMaxWidth(),
            onClick = {}

        )
    }
}
