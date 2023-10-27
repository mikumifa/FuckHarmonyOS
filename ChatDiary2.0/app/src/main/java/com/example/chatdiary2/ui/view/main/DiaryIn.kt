package com.example.chatdiary2.ui.view.main

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.chatdiary2.R
import com.example.chatdiary2.nav.Action
import com.example.chatdiary2.ui.theme.ChatDiary2Theme
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun DiaryIn(action: Action, paddingValues: PaddingValues) {

    ChatDiary2Theme {
        HomeScreen(action, paddingValues);

    }
}


@Composable
fun HomeScreen(action: Action, paddingValues: PaddingValues) {
    var navController = action.navController
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .offset(0.dp, (-30).dp),
            painter = painterResource(id = R.drawable.bg_main),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
            contentDescription = "Header Background",
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Title()
            Spacer(modifier = Modifier.height(20.dp))
            Content(action)
        }

    }
}

@Composable
fun Title() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "What would you like to\n" + "write diaries?\uD83D\uDE0B",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun Content(action: Action) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(action)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Every Day",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall
            )
            TextButton(onClick = {}) {
                Text(
                    text = "See all", color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowRight,
                    contentDescription = "Localized description",
                    modifier = Modifier.padding(end = 8.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }


        }
        val diaryVoList = listOf<dayDiaryVo>(
            dayDiaryVo(Date(), "example title", "example postion"),
            dayDiaryVo(Date(), "example title  long longlong", "example postion long long"),
            dayDiaryVo(Date(), "example title", "example postion"),
            dayDiaryVo(Date(), "example title", "example postion"),
            dayDiaryVo(Date(), "example title", "example postion"),
            dayDiaryVo(Date(), "example title", "example postion"),
            dayDiaryVo(Date(), "example title", "example postion"),
        )


        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    items(diaryVoList.size) { item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp)
                                    .background(color = MaterialTheme.colorScheme.secondaryContainer),
                                horizontalAlignment = CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                diaryVoList[item].image(
                                    Modifier.size(100.dp),
                                )

                                Text(
                                    modifier = Modifier
                                        .padding(start = 8.dp, end = 8.dp), // 可选：添加额外的边距
                                    text = diaryVoList[item].title,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1, // 设置为1，以显示单行文本
                                    overflow = TextOverflow.Ellipsis, // 使用省略号表示文本溢出
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )

                                Text(
                                    modifier = Modifier
                                        .padding(start = 8.dp, end = 8.dp),
                                    text = diaryVoList[item].position,
                                    fontWeight = FontWeight.Normal,
                                    style = MaterialTheme.typography.labelMedium,
                                    maxLines = 1, // 设置为1，以显示单行文本
                                    overflow = TextOverflow.Ellipsis, // 使用省略号表示文本溢出
                                    color = Color.Gray
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    MaterialTheme.colorScheme.onSecondaryContainer,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            ) {
                                                append(dateToYearMonthDayString(diaryVoList[item].date))
                                            }
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer

                                    )


                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                                            .padding(4.dp)
                                            .clickable { },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        var isExpanded by remember { mutableStateOf(false) }
                                        Icon(
                                            modifier = Modifier
                                                .size(20.dp, 20.dp)
                                                .clickable { isExpanded = true },
                                            imageVector = Icons.Default.MoreHoriz,
                                            contentDescription = "more",
                                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                        DropdownMenu(
                                            modifier = Modifier.background(
                                                MaterialTheme.colorScheme.primary,
                                                shape = RoundedCornerShape(8.dp)
                                            ),
                                            expanded = isExpanded,
                                            onDismissRequest = { isExpanded = false },
                                        ) {
                                            DropdownMenuItem(
                                                leadingIcon = {
                                                    Icon(
                                                        modifier = Modifier
                                                            .size(20.dp, 20.dp),
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = "edit",
                                                        tint = MaterialTheme.colorScheme.onPrimary
                                                    )
                                                },
                                                text = {
                                                    Text("Edit",
                                                        color =MaterialTheme.colorScheme.onPrimary )
                                                }, onClick = {
                                                    // 处理修改操作
                                                    isExpanded = false
                                                })
                                            DropdownMenuItem(

                                                leadingIcon = {
                                                    Icon(
                                                        modifier = Modifier
                                                            .size(20.dp, 20.dp),

                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "delete",
                                                        tint = MaterialTheme.colorScheme.onPrimary
                                                    )
                                                },
                                                text = {
                                                    Text("Delete",
                                                        color =MaterialTheme.colorScheme.onPrimary)
                                                },
                                                onClick = {
                                                    // 处理删除操作
                                                    isExpanded = false
                                                })
                                        }
                                    }

                                }
                            }
                        }
                    }
                })
        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(action: Action) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .height(120.dp)
                .width(200.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            IconButton(modifier = Modifier
                .align(CenterHorizontally)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.tertiaryContainer),
                onClick = { action.toDiary() {} }) {

                Column {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .height(80.dp)
                            .width(100.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Start",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }

}

data class dayDiaryVo(
    val date: Date,

    val title: String,
    val position: String,
    val image: @Composable (modifier: Modifier) -> Unit = {
        Image(
            modifier = it,
            imageVector = Icons.Default.BrokenImage,
            contentDescription = "",
            contentScale = ContentScale.Fit
        )
    },
)

private fun dateToYearMonthDayString(date: Date): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    return dateFormat.format(date)
}


@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen(
        action = Action(rememberNavController()),
        paddingValues = PaddingValues(bottom = 63.dp, top = 64.dp)
    )

}


@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun HomeScreenDarkPreview() {
    HomeScreen(
        action = Action(rememberNavController()),
        paddingValues = PaddingValues(bottom = 63.dp, top = 64.dp)
    )

}



