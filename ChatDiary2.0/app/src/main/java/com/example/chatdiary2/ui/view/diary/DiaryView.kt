package com.example.chatdiary2.ui.view.diary

import android.annotation.SuppressLint
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.chatdiary2.R
import com.example.chatdiary2.nav.Action
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DiaryView(action: Action) {

    var searchText by rememberSaveable { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Scaffold(

        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceVariant)) {
                    Title()
                    Divider(
                        color = Color.Gray, // 分割线颜色
                        thickness = 0.5.dp, // 分割线厚度
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp) // 填充整个宽度
                    )
                    Search(searchText, onQueryChange = { }, onSearch = {})
                }
                LazyColumn(
                    Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(3) {
                        DiaryItem("2023.12.14", "这是一个例子1", "南京", "TEXT", listOf("", ""))
                        DiaryItem("2023.12.14", "这是一个例子2", "南京", "TEXT", listOf("", ""))
                        DiaryItem("2023.12.14", "这是一个例子3", "南京", "TEXT", listOf("", ""))
                    }
                }

                InputDialog()
            }
        },
        floatingActionButton = {
            DatePickerDialogButton(
                onDateSelected = { newDate ->
                    selectedDate = newDate
                },
                onDismissRequest = {
                }
            )
        }
    )

}

@Composable
fun DatePicker(onDateSelected: (LocalDate) -> Unit, onDismissRequest: () -> Unit) {
    val selDate = remember { mutableStateOf(LocalDate.now()) }

    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    text = selDate.value.format(DateTimeFormatter.ofPattern("MMM d, YYYY")),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.size(16.dp))
            }

            CustomCalendarView(onDateSelected = {
                selDate.value = it
            })

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    //TODO - hardcode string
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                TextButton(
                    onClick = {
                        onDateSelected(selDate.value)
                        onDismissRequest()
                    }
                ) {
                    //TODO - hardcode string
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

            }
        }
    }
}

@Composable
fun DatePickerDialogButton(
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    var isDatePickerVisible by remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    FloatingActionButton(
        onClick = { isDatePickerVisible = true },
        content = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Open Date Picker"
            )
        },
        modifier = Modifier
            .padding(end = 8.dp, bottom = 90.dp).size(70.dp) // 向上移动按钮并放在右下角

    )
    if (isDatePickerVisible) {
        Dialog(
            onDismissRequest = {
                isDatePickerVisible = false
                onDismissRequest()
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            DatePicker(
                onDateSelected = {
                    selectedDate.value = it
                    isDatePickerVisible = false
                    onDateSelected(it)
                },
                onDismissRequest = {
                    isDatePickerVisible = false
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
fun CustomCalendarView(onDateSelected: (LocalDate) -> Unit) {
    // Adds view to Compose
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(ContextThemeWrapper(context, R.style.CalenderViewCustom))
        },
        update = { view ->
            view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                onDateSelected(
                    LocalDate
                        .now()
                        .withMonth(month + 1)
                        .withYear(year)
                        .withDayOfMonth(dayOfMonth)
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    query: String, onQueryChange: (String) -> Unit, onSearch: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(value = query, onValueChange = { newQuery ->
            onQueryChange(newQuery)
        }, modifier = Modifier.weight(1f), // 占据剩余空间
            label = { Text(text = "Search...") }, keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ), keyboardActions = KeyboardActions(onSearch = { onSearch() }), trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            })

    }
}


@Composable
fun Title() {
    Row(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .height(60.dp)
    ) {
        Text(
            text = "Diary",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold, fontSize = 36.sp // 设置字体大小
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDialog() {
    var text by rememberSaveable { mutableStateOf("") }
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            singleLine = false,
            modifier = Modifier
                .weight(1f) // 将文本输入框分配为 1 个权重，以填充剩余的空
                .padding(8.dp),
            shape = RoundedCornerShape(18.dp)

        )
        IconButton(
            onClick = { /*TODO*/ }, modifier = Modifier
                .size(48.dp)
                .background(
                    MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp)
                ) // 设置背景颜色
                .clip(CircleShape) // 将按钮设置为圆形
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_keyboard_voice_24), // 使用自定义图标
                contentDescription = "keyboard voice",
                tint = Color.White,
            )
        }
        Spacer(modifier = Modifier.width(4.dp)) // 可选的空白间隔
    }

}


@Composable
fun DiaryItem(
    date: String, info: String, pos: String, type: String, imageUrls: List<String>
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ), modifier = Modifier
            .height(250.dp)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 日期
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    // 类型
                    Text(
                        text = type,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }


                Spacer(modifier = Modifier.height(8.dp))

                // 信息
                Text(
                    text = info, style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier.weight(1f)) // 推动位置信息到末尾, 因为是Colum, 所以没有竖直方向的布局
                // 位置
                Text(
                    text = pos,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                )

                Spacer(modifier = Modifier.height(8.dp))

            }
        }
    }
}