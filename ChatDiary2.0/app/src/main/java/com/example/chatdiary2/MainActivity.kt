package com.example.chatdiary2

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatdiary2.nav.Action
import com.example.chatdiary2.nav.Destination
import com.example.chatdiary2.ui.theme.ChatDiary2Theme
import com.example.chatdiary2.ui.view.diary.DiaryView
import com.example.chatdiary2.ui.view.login.LoginView
import com.example.chatdiary2.ui.view.login.RegisterView
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val actions = remember(navController) {
                Action(navController)
            }
            NavHost(navController = navController, startDestination = Destination.Login) {
                composable(Destination.Login) {
                    LoginView(action = actions)
                }
                composable(Destination.Register) {
                    RegisterView(action = actions)
                }
                composable(Destination.Diary) {
                    DiaryView(action = actions)
                }

            }

        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES, name = "DefaultPreviewDark"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO, name = "DefaultPreviewLight"
)
@Composable
fun ReplyAppPreview() {
    val navController = rememberNavController()
    val actions = remember(navController) {
        Action(navController)
    }

    ChatDiary2Theme {
        DiaryView(actions)
    }
}