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
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatdiary2.delegate.SecureActivityDelegate
import com.example.chatdiary2.ui.theme.ChatDiaryTheme
import com.example.chatdiary2.ui.view.main.MainComponent
import com.example.chatdiary2.ui.view.main.diary.DiaryView
import com.example.chatdiary2.ui.view.main.diary.DiaryViewModel
import com.example.chatdiary2.ui.view.main.diary.SeeAllScreen
import com.example.chatdiary2.ui.view.main.genDiary.GenDiaryScreen
import com.example.chatdiary2.ui.view.main.login.AutoLoginScreen
import com.example.chatdiary2.ui.view.main.login.LoginView
import com.example.chatdiary2.ui.view.main.login.LoginViewModel
import com.example.chatdiary2.ui.view.main.login.RegisterView
import com.example.chatdiary2.ui.view.nav.Action
import com.example.chatdiary2.ui.view.nav.Destination
import com.example.chatdiary2.ui.view.settings.appearance.AppearanceScreen
import com.example.chatdiary2.ui.view.settings.lock.LockScreen
import com.example.chatdiary2.ui.view.settings.profile.profileScreen
import com.example.chatdiary2.util.secure.PreferenceStore
import com.example.chatdiary2.util.secure.SecurityPreferences
import com.example.chatdiary2.util.secure.UnlockActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), DefaultLifecycleObserver {

    @Inject
    lateinit var preferenceStore: PreferenceStore

    @Inject
    lateinit var preferences: SecurityPreferences

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("MutableCollectionMutableState", "CommitPrefEdits")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        //
        super<AppCompatActivity>.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
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
            val sharedPreferences: SharedPreferences =
                getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val isLoggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)
            val encryptionUtils = EncryptionUtils(this)
            val savedUsername = encryptionUtils.decrypt("email")
            val savedPassword = encryptionUtils.decrypt("password")
            NavHost(
                navController = navController,
                startDestination = if (isLoggedIn) Destination.Start else Destination.Login
//                startDestination = Destination.Appearance
            ) {
                composable(Destination.Login) {
                    sharedPreferences.edit().putBoolean("isLoggedIn", false)
                    if (savedUsername != "" && savedPassword != "") {
                        LoginView(
                            action = action,
                            password = savedPassword,
                            email = savedUsername,
                        )
                    } else {
                        LoginView(action = action)
                    }
                }
                composable(Destination.Start) {
                    AutoLoginScreen(
                        action = action, password = savedPassword, email = savedUsername
                    )
                }
                composable(Destination.Register) {
                    RegisterView(action = action)
                }

                composable(Destination.Diary) {
                    ChatDiaryTheme() {
                        DiaryView(action = action)
                    }
                }

                composable(Destination.Appearance) {
                    ChatDiaryTheme() {

                        AppearanceScreen(action = action)
                    }
                }
                composable(Destination.Main) {
                    ChatDiaryTheme() {

                        MainComponent(
                            action = action
                        )
                    }
                }
                composable(Destination.seeAllDiary) {
                    ChatDiaryTheme() {

                        SeeAllScreen(action = action)
                    }
                }
                composable(Destination.profile) {
                    ChatDiaryTheme() {
                        profileScreen(action = action)
                    }
                }
                composable(Destination.lock) {
                    ChatDiaryTheme() {

                        LockScreen(action = action)
                    }
                }
                composable(
                    route = Destination.DiaryGenDetails + "/{param}",
                    arguments = listOf(navArgument("param") { type = NavType.IntType })
                ) { entry ->
                    val param = entry.arguments?.getInt("param")
                    ChatDiaryTheme() {
                        GenDiaryScreen(
                            action = action, idx = param!!
                        )
                    }
                }
            }
        }

    }

    override fun onStart(owner: LifecycleOwner) {
        Log.d("lifecycle", "onStart: start")
        SecureActivityDelegate.onApplicationStart(preferences = preferences)
    }

    override fun onStop(owner: LifecycleOwner) {
        Log.d("lifecycle", "onStop: stop")
        SecureActivityDelegate.onApplicationStopped(preferences = preferences)
    }

    private var isInUnlockActivity: Boolean = false
    override fun onResume(owner: LifecycleOwner) {
        Log.d("lifecycle", "onResume: resume")
        if (!preferences.useAuthenticator().get()) return
        if (!SecureActivityDelegate.requireUnlock) return// 防止重复
        if (isInUnlockActivity) return
        isInUnlockActivity = true
        this.startActivity(Intent(this, UnlockActivity::class.java))
        this.overridePendingTransition(0, 0)
    }


}


