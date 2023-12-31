package com.example.chatdiary.ui.view.main.genDiary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatdiary.R
import com.example.chatdiary.ui.view.common.AnimatedPreloader
import com.example.chatdiary.ui.view.main.diary.DiaryViewModel
import com.example.chatdiary.ui.view.common.HorizontalImageList
import com.example.chatdiary.ui.view.nav.Action


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GenDiaryScreen(
    action: Action, idx: Int, diaryViewModel: DiaryViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    Scaffold(modifier = Modifier.semantics {
        testTagsAsResourceId = true
    },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.diary))
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
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(color = MaterialTheme.colorScheme.background)
        ) {

            diaryViewModel.getGenData.observe(lifecycleOwner) {
                if (it != null) diaryViewModel.genDiaryList.value = it;
            }
            if (diaryViewModel.genDiaryList.value.isEmpty()) {
                AnimatedPreloader(lottieSource = R.raw.loading)
            } else {
                val dayDiaryVo = diaryViewModel.genDiaryList.value.get(idx)
                val articleTitle = dayDiaryVo.title
                val articleContent = dayDiaryVo.content
                val imageUrls = dayDiaryVo.images;
                Divider(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                HorizontalImageList(imageUrls = imageUrls,
                    modifier = Modifier.padding(start = 14.dp),
                    onClick = {})
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
                        contentDescription = stringResource(id = R.string.author),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(id = R.string.date),
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        text = dayDiaryVo.date.toString(),
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Divider(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
                                thickness = 2.dp,
                                modifier = Modifier.width(160.dp)
                            )
                            Text(
                                text = "END", style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onBackground.copy(
                                        alpha = 0.50f
                                    )
                                ), modifier = Modifier
                            )
                            Divider(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
                                thickness = 2.dp,
                                modifier = Modifier.width(160.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}