package com.example.chatdiary.ui.view.nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BarItem(
    val label: String,
    val icon: @Composable (Boolean) -> Unit,
    val context: @Composable (it: PaddingValues) -> Unit
)


@Composable
fun BottomBar(
    action: Action, selectedIndex: MutableState<Int>, navList: Array<BarItem>
) {
    BottomNavigation(elevation = 0.dp) {
        navList.forEachIndexed { index, it ->
            BottomNavigationItem(
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        it.icon(selectedIndex.value == index) // 图标
                        AnimatedVisibility(visible = selectedIndex.value == index) {
                            Text(
                                text = it.label,
                                style = MaterialTheme.typography.labelLarge
                            ) // 图标上方的文字
                        }
                    }
                },
                label = { /* 可以选择不再显示标签，或者在这里添加其他内容 */ },
                selected = (selectedIndex.value == index),
                onClick = {
                    selectedIndex.value = index
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp) // 增加垂直方向的触摸范围
            )
        }
    }


}

@Preview
@Composable
fun BottomNavigationItemPreview() {
    BottomNavigationItem(icon = {
        // Add your icon Composable here
        Icon(
            imageVector = Icons.Default.Favorite, contentDescription = "Navigation Icon"
        )
    }, label = {
        Text(text = "Home")
    }, selected = true, onClick = { /* Handle the click event here */ }, modifier = Modifier
    )
}

@Composable
fun BottomNavigationItem(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.clickable(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
            if (selected) {
                Spacer(modifier = Modifier.height(4.dp))
                label()
            }
        }
    }
}

@Preview
@Composable
fun TopBarPreView() {

    TopBar("hello", {}, {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    info: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.secondary,
        ),
        title = {
            Text(
                info, maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
    )
}

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    elevation: Dp = 4.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(), color = backgroundColor, tonalElevation = elevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            content()
        }
    }
}


