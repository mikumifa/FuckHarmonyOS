package com.example.chatdiary2.ui.view.main.diary

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.chatdiary2.R
import com.example.chatdiary2.ui.view.common.AnimatedPreloader
import com.example.chatdiary2.ui.view.nav.Action
import com.example.chatdiary2.ui.view.nav.Destination
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun DiaryIn(action: Action, paddingValues: PaddingValues, diaryViewModel: DiaryViewModel) {
    HomeScreen(action, paddingValues, diaryViewModel);
}


@Composable
fun HomeScreen(
    action: Action, paddingValues: PaddingValues, diaryViewModel: DiaryViewModel = hiltViewModel()
) {
    var navController = action.navController
    val lifecycleOwner = LocalLifecycleOwner.current
    val isErrorShow = remember { mutableStateOf(false) }
    var errorShowInfo by remember { mutableStateOf("") }

    TimedDialog(showDialog = isErrorShow,
        durationMillis = 1000,
        text = errorShowInfo,
        onDismiss = {})


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
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondaryContainer),
            contentDescription = "Header Background",
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Title()
            Spacer(modifier = Modifier.height(20.dp))
            diaryViewModel.getGenData.observe(lifecycleOwner) {
                if (it != null) diaryViewModel.genDiaryList.value = it;
                else {
                    isErrorShow.value = true
                    errorShowInfo = "获取日记数据信息失败，请检查网络"
                }
            }
            Content(action, diaryViewModel.genDiaryList.value)
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
            text = "你想要写一个日记吗？\uD83D\uDE0B",
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun Content(action: Action, diaryVoList: List<DayDiaryVo>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(action)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "日记列表",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall
            )
            TextButton(onClick = { action.navController.navigate(Destination.seeAllDiary) }) {
                Text(
                    text = "全部", color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowRight,
                    contentDescription = "Localized description",
                    modifier = Modifier.padding(end = 8.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        DiaryListContent(action = action, modifier = Modifier.weight(1f), diaryVoList = diaryVoList)

    }
}

@Composable
fun DiaryListContent(action: Action, modifier: Modifier = Modifier, diaryVoList: List<DayDiaryVo>) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if (diaryVoList.isEmpty()) {
            AnimatedPreloader(lottieSource = R.raw.empty)
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    items(diaryVoList.size) { item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = 1.dp, color = MaterialTheme.colorScheme.surface
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = MaterialTheme.colorScheme.secondary)
                                    .clickable {
                                        action.navController.navigate(Destination.DiaryGenDetails + "/${item}")
                                    },
                                horizontalAlignment = CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                if (diaryVoList[item].images.isEmpty()) {
                                    Box(modifier = Modifier.height(160.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .height(140.dp)
                                                .fillMaxWidth()
                                                .padding(end = 15.dp)
                                                .align(Alignment.Center)
                                        ) {
                                            AnimatedPreloader(lottieSource = R.raw.no_image)
                                        }
                                    }
                                } else {
                                    val imageUrl = diaryVoList[item].images.get(0)
                                    AsyncImage(
                                        model = imageUrl,
                                        contentDescription = imageUrl,
                                        modifier = Modifier.height(160.dp)
                                    )
                                }


                                Text(
                                    modifier = Modifier.padding(
                                        start = 8.dp, end = 8.dp
                                    ), // 可选：添加额外的边距
                                    text = diaryVoList[item].title,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1, // 设置为1，以显示单行文本
                                    overflow = TextOverflow.Ellipsis, // 使用省略号表示文本溢出
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                    text = diaryVoList[item].content,
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
                                                    MaterialTheme.colorScheme.onSurface,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            ) {
                                                append(dateToYearMonthDayString(diaryVoList[item].date))
                                            }
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.surface

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
                                            contentDescription = "更多",
                                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
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
                .background(MaterialTheme.colorScheme.primaryContainer),
                onClick = { action.toDiary() {} }) {

                Column {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .height(80.dp)
                            .width(100.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "写日记",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }

}

data class DayDiaryVo(
    val date: Date, val id: Long, val content: String, val title: String, val images: List<String>

)

@SuppressLint("SimpleDateFormat")
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



