package com.bowoon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.bowoon.model.Product
import com.bowoon.ui.image.DynamicAsyncImageLoader
import com.bowoon.ui.utils.dp10
import com.bowoon.ui.utils.dp5
import com.bowoon.ui.utils.sp20

@Composable
fun ProductImageComponent(
    modifier: Modifier,
    product: Product,
    isFavorite: Boolean,
    addFavorite: () -> Unit,
    removeFavorite: () -> Unit
) {
    Box {
        DynamicAsyncImageLoader(
            source = product.image ?: "",
            contentDescription = product.image,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
        if (product.isSoldOut == true) {
            Box(
                modifier = modifier.semantics { contentDescription = "SoldOutBox" }.clip(RoundedCornerShape(dp10)).background(Color(0x667F7F7F)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SoldOut",
                    fontSize = sp20,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
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