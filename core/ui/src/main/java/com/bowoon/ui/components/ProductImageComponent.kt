package com.bowoon.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bowoon.ui.image.DynamicAsyncImageLoader
import com.bowoon.ui.utils.dp5

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
            contentDescription = source,
            modifier = modifier
        )
        FavoriteButtonComponent(
            modifier = Modifier.padding(all = dp5)
                .align(Alignment.TopEnd)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { if (isFavorite) removeFavorite() else addFavorite() },
            isFavorite = isFavorite
        )
    }
}