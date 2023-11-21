package com.example.chatdiary2.ui.view.diary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.chatdiary2.nav.Action
import java.util.Date

@Composable
@Preview
fun SeeAllScreenPreview() {
    val navController = rememberNavController()
    val actions = remember(navController) {
        Action(navController)
    }
    SeeAllScreen(action = actions)
}
@Composable
fun SeeAllScreen(action: Action) {
    val diaryVoList = listOf<dayDiaryVo>(
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title  long longlong", "example postion long long"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),
        dayDiaryVo(Date(), "example title", "example postion"),

    )

    Column (modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)){
        DiaryListContent(Modifier.weight(1f), diaryVoList)
    }
}