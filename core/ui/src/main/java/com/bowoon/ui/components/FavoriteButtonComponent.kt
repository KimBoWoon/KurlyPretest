package com.bowoon.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.bowoon.kurlypretest.core.ui.R
import com.bowoon.ui.utils.dp5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteButtonComponent(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    if (isFavorite) {
        Icon(
            modifier = modifier.padding(all = dp5)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() },
            painter = painterResource(R.drawable.ic_btn_heart_on),
            contentDescription = "favorite",
            tint = null
        )
    } else {
        Icon(
            modifier = modifier.padding(all = dp5)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() },
            painter = painterResource(R.drawable.ic_btn_heart_off),
            contentDescription = "unFavorite",
            tint = null
        )
    }
}