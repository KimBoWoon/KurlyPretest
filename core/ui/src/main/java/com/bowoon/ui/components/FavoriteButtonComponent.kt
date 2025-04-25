package com.bowoon.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.bowoon.kurlypretest.core.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteButtonComponent(
    modifier: Modifier = Modifier,
    isFavorite: Boolean
) {
    if (isFavorite) {
        Icon(
            modifier = modifier,
            painter = painterResource(R.drawable.ic_btn_heart_on),
            contentDescription = "favorite",
            tint = null
        )
    } else {
        Icon(
            modifier = modifier,
            painter = painterResource(R.drawable.ic_btn_heart_off),
            contentDescription = "unFavorite",
            tint = null
        )
    }
}