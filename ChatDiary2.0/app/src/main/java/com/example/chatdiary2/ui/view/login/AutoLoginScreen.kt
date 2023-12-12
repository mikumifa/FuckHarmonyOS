package com.example.chatdiary2.ui.view.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.chatdiary2.R
import com.example.chatdiary2.ui.nav.Action
import com.example.chatdiary2.ui.view.common.AnimatedPreloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AutoLoginScreen(
    action: Action, password: String = "", email: String = "", loginViewModel: LoginViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val loginUser = loginViewModel.loginUser(email, password)
    loginUser.observe(lifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                it?.let {
                    action.toMain() {}
                } ?: run {
                    action.toLogin() {}
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

