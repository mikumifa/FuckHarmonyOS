package com.example.chatdiary.ui.view.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatdiary.R


@Composable
fun HorizontalImageList(
    imageUrls: List<String>, modifier: Modifier = Modifier, onClick: (String) -> Unit
) {
    if (imageUrls.isNotEmpty()) {
        LazyRow(
            modifier = modifier
                .height(180.dp)
                .fillMaxWidth()
        ) {
            items(imageUrls.size) { idx ->
                val imageUrl = imageUrls[idx]
                AsyncImage(
                    model = imageUrl,
                    contentDescription = imageUrl,
                    modifier = Modifier
                        .height(160.dp)
                        .padding(end = 2.dp, start = 2.dp)
                        .clickable() {
                            onClick(imageUrl)
                        },
                )
            }
        }
    } else {
        Box(modifier = Modifier.height(180.dp)) {
            AnimatedPreloader(lottieSource = R.raw.no_image)
        }
    }
}