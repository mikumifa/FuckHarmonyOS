package com.example.chatdiary.ui.view.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlin.math.max
import kotlin.math.min


@Composable
fun ZoomableImage(modifier: Modifier = Modifier, imageUrl: String, onClose: () -> Unit) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(imageUrl).crossfade(true).build()
    )

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "关闭",
            modifier = Modifier
                .padding(16.dp)
                .padding(top = 50.dp)
                .size(48.dp)
                .background(
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f),
                    shape = CircleShape
                )
                .clickable(onClick = onClose), tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Image(painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 3f)
                        offset += pan
                    }
                }
                .graphicsLayer(
                    scaleX = max(1f, min(3f, scale)),
                    scaleY = max(1f, min(3f, scale)),
                    translationX = offset.x,
                    translationY = offset.y
                ))

    }
}

@Composable
@Preview
fun ZoomableImagePreview() {
    ZoomableImage(modifier = Modifier.fillMaxSize(),
        imageUrl = "https://inews.gtimg.com/om_ls/OjrssaQ_WIdG4A7Oo3rikAeWgDmJ7KDlxkUGJ1tWTrlm8AA_870492/0",
        {})
}