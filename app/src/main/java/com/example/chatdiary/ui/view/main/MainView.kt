package com.example.chatdiary.ui.view.main

import HappyValueScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatdiary.R
import com.example.chatdiary.ui.view.common.AnimationIconButton
import com.example.chatdiary.ui.view.main.chat.ChatScreen
import com.example.chatdiary.ui.view.main.diary.DiaryIn
import com.example.chatdiary.ui.view.main.diary.DiaryViewModel
import com.example.chatdiary.ui.view.main.sideDrawer.DrawerContent
import com.example.chatdiary.ui.view.nav.Action
import com.example.chatdiary.ui.view.nav.BarItem
import com.example.chatdiary.ui.view.nav.BottomBar
import com.example.chatdiary.ui.view.nav.TopBar
import com.example.chatdiary.ui.view.settings.profile.ProfileScreenViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainComponent(
    action: Action,
    profileScreenViewModel: ProfileScreenViewModel,
    diaryViewModel: DiaryViewModel = hiltViewModel(),
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedIndex = remember { mutableStateOf(0) }


    // 定义动画状态
    val scale by animateFloatAsState(
        targetValue = if (drawerState.isClosed) 1f else 0.99f, animationSpec = tween(
            durationMillis = 300, // 动画时长
            easing = FastOutSlowInEasing // 插值器
        ), label = ""
    )
    val offsetX by animateFloatAsState(
        targetValue = if (drawerState.isClosed) 0f else 20f, animationSpec = tween(
            durationMillis = 300, easing = FastOutSlowInEasing
        ), label = ""
    )

    val value = arrayOf(BarItem(stringResource(id = R.string.bar_diary), {
        AnimationIconButton(
            resInt = R.raw.file,
            isSelected = it,
            speed = 1f,
            modifier = Modifier.size(50.dp, 50.dp)
        )
    }) {
        DiaryIn(
            action = action, it, diaryViewModel
        )
    },

        BarItem(stringResource(id = R.string.barat), {
            AnimationIconButton(
                resInt = R.raw.texting,
                isSelected = it,
                speed = 1f,
                modifier = Modifier.size(50.dp, 50.dp)
            )
        }) {
            Box(modifier = Modifier.padding(it)) {
                ChatScreen(action = action, profileScreenViewModel = profileScreenViewModel)
            }
        },

        BarItem(stringResource(id = R.string.bar_statistic), {
            AnimationIconButton(
                resInt = R.raw.smile_and_sad_emoji,
                isSelected = it,
                speed = 1f,
                modifier = Modifier.size(50.dp, 50.dp)
            )
        }) {
            HappyValueScreen(modifier = Modifier.padding(it), onDrawerClicked = { -> })
        })
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        AnimatedVisibility(visible = drawerState.isOpen) {
            DrawerContent(actions = action) {}
        }
    }, content = {
        Scaffold(
            //graphicsLayer can be used to apply effects to content, such as scaling, rotation, opacity, shadow, and clipping.
            modifier = Modifier.graphicsLayer {
                scaleX = scale //底层渲染， x轴比例
                scaleY = scale
                translationX = offsetX //正是右移
            }, topBar = {
                TopBar(value[selectedIndex.value].label, {
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