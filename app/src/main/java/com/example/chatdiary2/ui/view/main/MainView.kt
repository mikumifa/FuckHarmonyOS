package com.example.chatdiary2.ui.view.main

import HappyValueScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Note
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatdiary2.R
import com.example.chatdiary2.ui.view.main.chat.ChatScreen
import com.example.chatdiary2.ui.view.main.diary.DiaryIn
import com.example.chatdiary2.ui.view.main.diary.DiaryViewModel
import com.example.chatdiary2.ui.view.main.sideDrawer.DrawerContent
import com.example.chatdiary2.ui.view.nav.Action
import com.example.chatdiary2.ui.view.nav.BarItem
import com.example.chatdiary2.ui.view.nav.BottomBar
import com.example.chatdiary2.ui.view.nav.TopBar
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainComponent(
    action: Action,
    diaryViewModel: DiaryViewModel = hiltViewModel(),
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedIndex = remember { mutableStateOf(0) }
    val value = arrayOf(
        BarItem(
            Icons.Default.Note,
            stringResource(id = R.string.bar_diary)
        ) { DiaryIn(action = action, it, diaryViewModel) },
        BarItem(Icons.Default.Chat,   stringResource(id = R.string.barat)) {
            Box(modifier = Modifier.padding(it)) {
                ChatScreen(action = action)
            }
        },

        BarItem(Icons.Filled.BrightnessHigh,  stringResource(id = R.string.bar_statistic)) {
            HappyValueScreen(modifier = Modifier.padding(it), onDrawerClicked = { -> })
        })
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        DrawerContent(actions = action) {}
    }, content = {
        Scaffold(topBar = {
            TopBar(value[selectedIndex.value].text, {
                IconButton(onClick = {
                    scope.launch { drawerState.open() }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Localized description"
                    )
                }
            }, {
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = Icons.Filled.MoreHoriz,
                        contentDescription = "Localized description"
                    )
                }
            })

        }, bottomBar = {
            BottomBar(action, selectedIndex, value)
        }, content = {
            value[selectedIndex.value].context(it)
        })
    })
}