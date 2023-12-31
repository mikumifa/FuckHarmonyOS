package com.example.chatdiary

import EncryptionUtils
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.TransformOrigin
import androidx.core.view.WindowCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatdiary.delegate.SecureActivityDelegate
import com.example.chatdiary.ui.theme.ChatDiaryTheme
import com.example.chatdiary.ui.view.main.MainComponent
import com.example.chatdiary.ui.view.main.diary.DiaryView
import com.example.chatdiary.ui.view.main.diary.SeeAllScreen
import com.example.chatdiary.ui.view.main.genDiary.GenDiaryScreen
import com.example.chatdiary.ui.view.main.login.AutoLoginScreen
import com.example.chatdiary.ui.view.main.login.LoginView
import com.example.chatdiary.ui.view.main.login.RegisterView
import com.example.chatdiary.ui.view.nav.Action
import com.example.chatdiary.ui.view.nav.Destination
import com.example.chatdiary.ui.view.settings.appearance.AppearanceScreen
import com.example.chatdiary.ui.view.settings.lock.LockScreen
import com.example.chatdiary.ui.view.settings.profile.ProfileScreenViewModel
import com.example.chatdiary.ui.view.settings.profile.profileScreen
import com.example.chatdiary.util.secure.PreferenceStore
import com.example.chatdiary.util.secure.SecurityPreferences
import com.example.chatdiary.util.secure.UnlockActivity
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
        val profileScreenViewModel: ProfileScreenViewModel by viewModels()
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

                composable(
                    Destination.Diary,
                    enterTransition = { slideInHorizontally() { it } },
                    exitTransition = { slideOutHorizontally() { it } },
                    popEnterTransition = { slideInHorizontally() { it } },
                    popExitTransition = { slideOutHorizontally() { it } },
                ) {
                    ChatDiaryTheme() {
                        DiaryView(action = action)
                    }
                }

                composable(
                    Destination.Appearance,
                    enterTransition = { slideInHorizontally() { it } },
                    exitTransition = { slideOutHorizontally() { it } },
                    popEnterTransition = { slideInHorizontally() { it } },
                    popExitTransition = { slideOutHorizontally() { it } },
                ) {
                    ChatDiaryTheme() {

                        AppearanceScreen(action = action)
                    }
                }
                composable(Destination.Main) {
                    ChatDiaryTheme() {

                        MainComponent(
                            profileScreenViewModel = profileScreenViewModel,
                            action = action
                        )
                    }
                }
                composable(
                    Destination.seeAllDiary, enterTransition = { slideInHorizontally() { it } },
                    exitTransition = { slideOutHorizontally() { it } },
                    popEnterTransition = { slideInHorizontally() { it } },
                    popExitTransition = { slideOutHorizontally() { it } },
                ) {
                    ChatDiaryTheme() {

                        SeeAllScreen(action = action)
                    }
                }
                composable(
                    Destination.profile,
                    enterTransition = { slideInHorizontally() { it } },
                    exitTransition = { slideOutHorizontally() { it } },
                    popEnterTransition = { slideInHorizontally() { it } },
                    popExitTransition = { slideOutHorizontally() { it } },
                ) {

                    ChatDiaryTheme() {
                        profileScreen(action = action, profileViewModel = profileScreenViewModel)
                    }
                }
                composable(
                    Destination.lock,
                    enterTransition = { slideInHorizontally() { it } },
                    exitTransition = { slideOutHorizontally() { it } },
                    popEnterTransition = { slideInHorizontally() { it } },
                    popExitTransition = { slideOutHorizontally() { it } },
                ) {
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
        if (isInUnlockActivity) return // 防止重复
        isInUnlockActivity = true
        val requestCode = 123
        this.startActivityForResult(Intent(this, UnlockActivity::class.java), requestCode)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            isInUnlockActivity = false
        }
    }


}


