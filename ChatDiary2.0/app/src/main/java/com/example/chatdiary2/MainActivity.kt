package com.example.chatdiary2

import EncryptionUtils
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatdiary2.nav.Action
import com.example.chatdiary2.nav.Destination
import com.example.chatdiary2.ui.theme.ChatDiary2Theme
import com.example.chatdiary2.ui.view.diary.DiaryView
import com.example.chatdiary2.ui.view.diary.SeeAllScreen
import com.example.chatdiary2.ui.view.login.LoginView
import com.example.chatdiary2.ui.view.login.RegisterView
import com.example.chatdiary2.ui.view.main.MainComponent

import dagger.hilt.android.AndroidEntryPoint
import java.io.File

data class SelectedImage(
    val id: String,
    val path: String,
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @SuppressLint("MutableCollectionMutableState")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
        requestPermissions(arrayOf(android.Manifest.permission.RECORD_AUDIO), 100)
        requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_VIDEO), 100)
        requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), 100)
        requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()
        setContent {
            val navController = rememberNavController()
            val actions = remember(navController) {
                Action(navController)
            }
            val sharedPreferences: SharedPreferences =
                getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val isLoggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)
            val encryptionUtils = EncryptionUtils(this)
            NavHost(navController = navController, startDestination = Destination.Login) {
                composable(Destination.Login) {
                    if (isLoggedIn) {
                        val savedUsername = encryptionUtils.decrypt("email")
                        val savedPassword = encryptionUtils.decrypt("password")
                        LoginView(action = actions, password = savedPassword, email = savedUsername)
                    } else {
                        LoginView(action = actions)
                    }
                }
                composable(Destination.Register) {

                    RegisterView(action = actions)
                }
                composable(Destination.Diary) {
                    ChatDiary2Theme {
                        DiaryView(action = actions)
                    }
                }
                composable(Destination.Main) {
                    ChatDiary2Theme {
                        MainComponent(action = actions)
                    }
                }
                composable(Destination.seeAllDiary) {
                    ChatDiary2Theme {
                        SeeAllScreen(action = actions)
                    }
                }
                composable(Destination.assertPicker) {

                }
            }
        }

    }
}


