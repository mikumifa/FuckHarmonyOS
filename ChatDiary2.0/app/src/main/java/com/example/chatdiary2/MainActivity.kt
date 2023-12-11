package com.example.chatdiary2

import EncryptionUtils
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatdiary2.delegate.SecureActivityDelegate
import com.example.chatdiary2.ui.nav.Action
import com.example.chatdiary2.ui.nav.Destination
import com.example.chatdiary2.ui.theme.ChatDiary2Theme
import com.example.chatdiary2.ui.view.diary.DiaryView
import com.example.chatdiary2.ui.view.diary.DiaryViewModel
import com.example.chatdiary2.ui.view.diary.SeeAllScreen
import com.example.chatdiary2.ui.view.genDiary.GenDiaryScreen
import com.example.chatdiary2.ui.view.login.AutoLoginScreen
import com.example.chatdiary2.ui.view.login.LoginView
import com.example.chatdiary2.ui.view.login.LoginViewModel
import com.example.chatdiary2.ui.view.login.RegisterView
import com.example.chatdiary2.ui.view.main.MainComponent
import com.example.chatdiary2.ui.view.profile.profileScreen
import com.example.chatdiary2.ui.view.settings.lock.LockScreen
import com.example.chatdiary2.util.secure.PreferenceStore
import com.example.chatdiary2.util.secure.SecurityPreferences
import com.example.chatdiary2.util.secure.UnlockActivity

import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity(), DefaultLifecycleObserver {

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("MutableCollectionMutableState", "CommitPrefEdits")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super<ComponentActivity>.onCreate(savedInstanceState)
        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
        requestPermissions(arrayOf(android.Manifest.permission.RECORD_AUDIO), 100)
        requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_VIDEO), 100)
        requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), 100)
        requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        setContent {
            val navController = rememberNavController()
            val action = remember(navController) {
                Action(navController)
            }
            val diaryViewModel by viewModels<DiaryViewModel>()
            val loginViewModel by viewModels<LoginViewModel>()

            val sharedPreferences: SharedPreferences =
                getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val isLoggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)
            val encryptionUtils = EncryptionUtils(this)
            val savedUsername = encryptionUtils.decrypt("email")
            val savedPassword = encryptionUtils.decrypt("password")
            NavHost(
                navController = navController,
                startDestination = if (isLoggedIn) Destination.Start else Destination.Login
            ) {
                composable(Destination.Login) {
                    sharedPreferences.edit().putBoolean("isLoggedIn", false)
                    if (savedUsername != "" && savedPassword != "") {
                        LoginView(
                            action = action,
                            password = savedPassword,
                            email = savedUsername,
                            loginViewModel = loginViewModel
                        )
                    } else {
                        LoginView(action = action, loginViewModel = loginViewModel)
                    }
                }
                composable(Destination.Start) {
                    AutoLoginScreen(
                        action = action,
                        password = savedUsername,
                        email = savedPassword,
                        loginViewModel = loginViewModel
                    )
                }
                composable(Destination.Register) {
                    RegisterView(action = action)
                }
                composable(Destination.Diary) {
                    ChatDiary2Theme {
                        DiaryView(action = action, diaryViewModel = diaryViewModel)
                    }
                }
                composable(Destination.Main) {
                    ChatDiary2Theme {
                        MainComponent(
                            action = action,
                            diaryViewModel = diaryViewModel,
                        )
                    }
                }
                composable(Destination.seeAllDiary) {
                    ChatDiary2Theme {
                        SeeAllScreen(action = action, diaryViewModel = diaryViewModel)
                    }
                }
                composable(Destination.assertPicker) {

                }
                composable(Destination.profile) {
                    ChatDiary2Theme {
                        profileScreen(action = action)
                    }
                }
                composable(Destination.lock) {
                    ChatDiary2Theme {
                        LockScreen(action = action)
                    }
                }
                composable(
                    route = Destination.DiaryGenDetails + "/{param}",
                    arguments = listOf(navArgument("param") { type = NavType.IntType })
                ) { entry ->
                    val param = entry.arguments?.getInt("param")
                    ChatDiary2Theme {
                        GenDiaryScreen(
                            action = action, idx = param!!, diaryViewModel = diaryViewModel
                        )
                    }
                }
            }
        }

    }

    @Inject
    lateinit var preferenceStore: PreferenceStore

    @Inject
    lateinit var preferences: SecurityPreferences
    override fun onStart(owner: LifecycleOwner) {
        Log.d("lifecycle", "onStart: start")
        SecureActivityDelegate.onApplicationStart(preferences = preferences)
    }

    override fun onStop(owner: LifecycleOwner) {
        Log.d("lifecycle", "onStop: stop")
        SecureActivityDelegate.onApplicationStopped(preferences = preferences)
    }

    override fun onResume(owner: LifecycleOwner) {
        Log.d("lifecycle", "onResume: resume")

        if (!preferences.useAuthenticator().get()) return
        this.startActivity(Intent(this, UnlockActivity::class.java))
        this.overridePendingTransition(0, 0)
    }


}


