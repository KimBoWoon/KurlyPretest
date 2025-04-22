package com.bowoon.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.bowoon.ui.utils.dp5
import com.bowoon.ui.utils.sp13

@Composable
fun ProductTitleComponent(
    title: String,
    titleLine: Int
) {
    Text(
        modifier = Modifier.fillMaxWidth().padding(vertical = dp5),
        text = title,
        minLines = titleLine,
        maxLines = titleLine,
        overflow = TextOverflow.Ellipsis,
        fontSize = sp13,
        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
    )
}