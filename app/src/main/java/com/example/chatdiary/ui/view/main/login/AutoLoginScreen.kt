package com.example.chatdiary.ui.view.main.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.example.chatdiary.R
import com.example.chatdiary.ui.view.common.AnimatedPreloader
import com.example.chatdiary.ui.view.common.TransparentSystemBars
import com.example.chatdiary.ui.view.nav.Action
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AutoLoginScreen(
    action: Action,
    password: String = "",
    email: String = "",
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    TransparentSystemBars()
    val lifecycleOwner = LocalLifecycleOwner.current
    val loginUser = loginViewModel.loginUser(email, password)
    loginUser.observe(lifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                it?.let {
                    action.toMain() {
                        action.navController.currentBackStackEntry?.destination?.let { it1 ->
                            popUpTo(it1.id) {
                                inclusive = true
                            }
                        }

                    }
                } ?: run {
                    action.toLogin() {
                        action.navController.currentBackStackEntry?.destination?.let { it1 ->
                            popUpTo(it1.id) {
                                inclusive = true
                            }
                        }
                    }

                }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimatedPreloader(modifier = Modifier, lottieSource = R.raw.start)
    }
}

