package com.example.chatdiary.ui.view.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.chatdiary.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun AnimationIconButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    resInt: Int,
    speed: Float = 2.5f,
) {
    var isPlaying by remember { mutableStateOf(false) }
    //钩子， 在 isSelected 的值发生变化时重启动画
    LaunchedEffect(isSelected) {
        isPlaying = isSelected
    }
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            resInt
        )
    )
    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = 1,
        isPlaying = isPlaying,
        speed = speed,
        //isplay变化就开始
        restartOnPlay = true,
    )

    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = if (isSelected) preloaderProgress else 1f,
        modifier = modifier
    )
}

