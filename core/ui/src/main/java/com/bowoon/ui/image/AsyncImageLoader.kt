package com.bowoon.ui.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.bowoon.ui.utils.dp10

@Composable
fun DynamicAsyncImageLoader(
    source: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Painter = ColorPainter(Color.Gray),
    error: Painter = ColorPainter(Color.Gray)
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val imageLoader = rememberAsyncImagePainter(
        model = source,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        }
    )

    Box(
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            Image(
                modifier = modifier.testTag(tag = source).clip(RoundedCornerShape(dp10)),
                contentScale = contentScale,
                painter = placeholder,
                contentDescription = "CoilImageLoading",
            )
        }
        when (isError) {
            true -> {
                Image(
                    modifier = modifier.testTag(tag = source).clip(RoundedCornerShape(dp10)),
                    contentScale = ContentScale.Crop,
                    painter = error,
                    contentDescription = "CoilImageError",
                )
            }
            false -> {
                Image(
                    modifier = modifier.testTag(tag = source).clip(RoundedCornerShape(dp10)),
                    contentScale = contentScale,
                    painter = imageLoader,
                    contentDescription = "CoilImageSuccess",
                )
            }
        }
    }
}