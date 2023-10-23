package com.example.chatdiary2.ui.view.nav

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.chatdiary2.nav.Action

data class BarItem(
    val icon: ImageVector,
    val text: String,
    val context: @Composable (it: PaddingValues) -> Unit
)



@Composable
fun BottomBar(

    action: Action,
    selectedIndex: MutableState<Int>,
    navList: Array<BarItem>
) {
    BottomNavigation(elevation = 10.dp) {
        navList.forEachIndexed { index, it ->
            BottomNavigationItem(icon =
            { Icon(imageVector = it.icon, "") },
                label = { Text(text = it.text) },
                selected = (selectedIndex.value == index),
                onClick = {
                    selectedIndex.value = index
                })
        }
    }

}

@Preview
@Composable
fun BottomNavigationItemPreview() {
    BottomNavigationItem(
        icon = {
            // Add your icon Composable here
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Navigation Icon"
            )
        },
        label = {
            Text(text = "Home")
        },
        selected = true,
        onClick = { /* Handle the click event here */ }
    )
}

@Composable
fun BottomNavigationItem(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.clickable(onClick = onClick),
        contentAlignment = Alignment.Center
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
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                info,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor,
        tonalElevation = elevation
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


