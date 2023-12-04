package com.example.chatdiary2.ui.view.genDiary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatdiary2.ui.theme.ChatDiary2Theme
import com.example.chatdiary2.ui.view.diary.HorizontalImageList

@Composable
@Preview
fun GenDiaryScreenPreview() {
    ChatDiary2Theme {
        GenDiaryScreen()
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GenDiaryScreen() {
    Scaffold(modifier = Modifier.semantics {
        testTagsAsResourceId = true
    },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "日记")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // action.navController.navigateUp()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            val articleTitle = "开心的一天"
            val articleContent =
                "" + "\n" + "今天是一个平凡而温暖的一天。清晨的阳光透过窗帘洒进房间，悄悄唤醒了我。起床的时候，感觉到室外的寒意，于是穿上了一件舒适的毛衣。\n" + "\n" + "早餐是一杯热气腾腾的咖啡和一片软糯的吐司，搭配着果酱的香甜。在这宁静的时刻，我感受到一天的开始是多么的美好。\n" + "\n" + "上午的工作相当繁忙，但在忙碌中也有一丝充实感。同事们团队协作，共同努力，让工作变得更加有趣。午饭时，我们一起品尝了一家新开的餐厅，美味的菜肴让人胃口大开。\n" + "\n" + "下午，阳光逐渐变得柔和，我决定放慢脚步，去附近的公园散步。公园里的树木在微风中轻轻摇曳，落叶飘舞，仿佛在为我讲述一个个动人的故事。我找了一个长椅坐下，闭上眼睛，尽情享受大自然的馈赠。\n" + "\n" + "晚上，回到家里，准备了一顿简单而温馨的晚餐。烛光摇曳间，我感慨生活的美好。在这个温馨的夜晚，我决定给自己一点时间，静下心来读一本喜爱的书。\n" + "\n" + "总的来说，今天是一个充实而美好的一天。生活中的点滴细节都是那么温馨，让人感慨时光匆匆而美好。期待明天的到来，迎接新的挑战和美好。"
            val imageUrl =
                "https://gitee.com/misakabryant/chat-diary-fig/raw/master/ChatDiary/1701196018624.jpg"
            Divider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            HorizontalImageList(
                imageUrls = listOf(imageUrl, imageUrl, imageUrl, imageUrl, imageUrl),
                modifier = Modifier.padding(start = 14.dp)
            )
            Text(
                text = articleTitle,
                style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Row(horizontalArrangement = Arrangement.Center) {
                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = "作者",
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = " 时间: ",
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = "2023年12月4日",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.50f
                        ),
                    ),
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .align(Alignment.CenterVertically),
                )
            }
            Divider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            LazyColumn {
                item {
                    Text(
                         text = articleContent,
                        //text = articleTitle,
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Divider(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
                            thickness = 2.dp,
                            modifier = Modifier
                                .width(160.dp)
                        )
                        Text(
                            text = "END",
                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.50f)),
                            modifier = Modifier
                        )
                        Divider(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
                            thickness = 2.dp,
                            modifier = Modifier
                                .width(160.dp)
                        )
                    }
                }
            }
        }
    }
}