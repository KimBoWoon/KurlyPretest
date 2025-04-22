package com.bowoon.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bowoon.ui.image.DynamicAsyncImageLoader

@Composable
fun ProductImageComponent(
    modifier: Modifier,
    source: String,
    isFavorite: Boolean,
    addFavorite: () -> Unit,
    removeFavorite: () -> Unit
) {
    Box {
        DynamicAsyncImageLoader(
            source = source,
            contentDescription = null,
            modifier = modifier
        )
        FavoriteButtonComponent(
            modifier = Modifier.align(Alignment.TopEnd),
            isFavorite = isFavorite,
            onClick = {
                if (isFavorite) removeFavorite() else addFavorite()
            }
        )
    }
}