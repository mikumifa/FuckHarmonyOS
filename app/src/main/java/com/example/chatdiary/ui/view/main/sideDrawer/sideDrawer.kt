package com.example.chatdiary.ui.view.main.sideDrawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Looks
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.chatdiary.R
import com.example.chatdiary.data.UserVO
import com.example.chatdiary.ui.view.nav.Action
import com.example.chatdiary.ui.view.nav.Destination

object Menus {
    val list = arrayOf(

        DrawerMenu(Icons.Filled.Face, R.string.user_info) {
            it.navController.navigate(Destination.profile)
        },

        DrawerMenu(Icons.Filled.Lock, R.string.app_lock) {
            it.navController.navigate(Destination.lock)
        },
        DrawerMenu(Icons.Filled.Looks, R.string.appearance_language) {
            it.navController.navigate(Destination.Appearance)
        },
        //DrawerMenu(Icons.Filled.Settings, "Settings", Destination.Settings),
        DrawerMenu(Icons.Filled.Info, R.string.about_us) {

        },
        DrawerMenu(Icons.Filled.Logout, R.string.logout) {
            it.toLogin() {
                it.navController.currentBackStackEntry?.destination?.let { it1 ->
                    popUpTo(it1.id) {
                        inclusive = true
                    }
                }
            }
        },
    )
}

data class DrawerMenu(
    val icon: ImageVector, val title: Int, val onClick: (actions: Action) -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    actions: Action,
    menus: Array<DrawerMenu> = Menus.list,
    sideDrawerViewModel: SideDrawerViewModel = hiltViewModel(),
    onMenuClick: (String) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val userVOState = remember {
        mutableStateOf(
            UserVO(
                email = "加载中",
                username = "加载中",
                id = 0L,
                userInfo = "加载中",
                avatarUrl = null
            )
        )
    }
    val userVOLiveData = sideDrawerViewModel.getUserInfo()
    userVOLiveData.observe(lifecycleOwner) {
        if (it != null) {
            userVOState.value = it
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth(0.8f)
            .fillMaxHeight()
            .padding(16.dp)


    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxHeight()
                .align(Alignment.CenterHorizontally)

        ) {

            if (userVOState.value.avatarUrl == null) {
                Image(
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.Center),
                    imageVector = Icons.Filled.AccountCircle,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            } else {
                AsyncImage(model = userVOState.value.avatarUrl,
                    contentDescription = userVOState.value.avatarUrl,
                    modifier = Modifier
                        .height(150.dp)
                        .padding(end = 2.dp, start = 2.dp)
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            actions.navController.navigate(Destination.profile)
                        })
            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        var isDrawerVisible by remember { mutableStateOf(true) }
        menus.forEach {
            NavigationDrawerItem(label = { Text(text = stringResource(id = it.title)) },
                icon = { Icon(imageVector = it.icon, contentDescription = null) },
                selected = false,
                onClick = {
                    isDrawerVisible = false
                    it.onClick(actions)
                }

            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
    Spacer(modifier = Modifier.height(12.dp))

}

