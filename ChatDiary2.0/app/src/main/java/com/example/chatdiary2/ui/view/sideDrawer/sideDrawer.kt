package com.example.chatdiary2.ui.view.sideDrawer

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.chatdiary2.nav.Action
import com.example.chatdiary2.nav.Destination

object Menus {
    val list = arrayOf(

        DrawerMenu(Icons.Filled.Face, "Articles") {

        },
        //DrawerMenu(Icons.Filled.Settings, "Settings", Destination.Settings),
        DrawerMenu(Icons.Filled.Info, "About Us") {

        },
        DrawerMenu(Icons.Filled.Logout, "Log Out") {
            it.toLogin() {
                it.navController.currentBackStackEntry?.destination?.let { it1 ->
                    popUpTo(it1.id) {
                        inclusive = true
                    }
                }
            }
        }
    )
}

data class DrawerMenu(
    val icon: ImageVector,
    val title: String,
    val onClick: (actions: Action) -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    actions: Action,
    menus: Array<DrawerMenu> = Menus.list,
    onMenuClick: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(12.dp))

    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
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
            Image(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.Center),
                imageVector = Icons.Filled.AccountCircle,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        menus.forEach {
            NavigationDrawerItem(
                label = { Text(text = it.title) },
                icon = { Icon(imageVector = it.icon, contentDescription = null) },
                selected = false,
                onClick = {
                    it.onClick(actions)
                }

            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
    Spacer(modifier = Modifier.height(12.dp))

}

