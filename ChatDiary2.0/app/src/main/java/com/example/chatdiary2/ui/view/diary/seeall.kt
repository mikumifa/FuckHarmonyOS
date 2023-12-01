package com.example.chatdiary2.ui.view.diary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
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
    Scaffold(modifier = Modifier.semantics {
        testTagsAsResourceId = true
    },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "查看全部")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        action.navController.navigateUp()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
        ) {
            DiaryListContent(Modifier.weight(1f), diaryVoList)
        }
    }
}