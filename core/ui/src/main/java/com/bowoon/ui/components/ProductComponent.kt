package com.bowoon.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.bowoon.common.getDiscountRate
import com.bowoon.kurlypretest.core.ui.R
import com.bowoon.model.Product
import com.bowoon.model.SectionType
import com.bowoon.ui.utils.dp150
import com.bowoon.ui.utils.dp200
import com.bowoon.ui.utils.dp5
import com.bowoon.ui.utils.sp13
import com.bowoon.ui.utils.sp15

@Composable
fun ProductComponent(
    product: Product,
    type: SectionType,
    addFavorite: (Product) -> Unit,
    removeFavorite: (Product) -> Unit
) {
    Column(
        modifier = when (type) {
            SectionType.NONE -> Modifier
            SectionType.VERTICAL -> Modifier.fillMaxWidth()
            SectionType.HORIZONTAL, SectionType.GRID -> Modifier.width(width = dp150)
        }.semantics { contentDescription = "${product.name}_productComponent" }
    ) {
        ProductImageComponent(
            modifier = when (type) {
                SectionType.NONE -> Modifier
                SectionType.VERTICAL -> Modifier.fillMaxWidth().aspectRatio(6f / 4f)
                SectionType.HORIZONTAL, SectionType.GRID -> Modifier.fillMaxWidth().height(height = dp200)
            },
            source = product.image ?: "",
            isFavorite = product.isFavorite,
            addFavorite = { addFavorite(product) },
            removeFavorite = { removeFavorite(product) }
        )
        ProductTitleComponent(
            title = product.name ?: "",
            titleLine = when (type) {
                SectionType.NONE -> 1
                SectionType.VERTICAL -> 1
                SectionType.HORIZONTAL, SectionType.GRID -> 2
            }
        )
        if (product.discountedPrice != null) {
            val discountRate = getDiscountRate(product.originalPrice ?: 0, product.discountedPrice ?: 0)

            when (type) {
                SectionType.NONE -> {}
                SectionType.VERTICAL -> {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = dp5),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.discount_rate, discountRate.toInt()),
                            color = Color(0xFFFA622F),
                            fontSize = sp15,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false), fontWeight = FontWeight.Bold)
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = dp5),
                            text = stringResource(R.string.product_price, product.discountedPrice ?: 0),
                            fontSize = sp15,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false), fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = stringResource(R.string.product_price, product.originalPrice ?: 0),
                            textDecoration = TextDecoration.LineThrough,
                            fontSize = sp13,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                    }
                }
                SectionType.HORIZONTAL, SectionType.GRID -> {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = dp5)
                    ) {
                        Text(
                            modifier = Modifier.padding(end = dp5),
                            text = stringResource(R.string.discount_rate, discountRate.toInt()),
                            color = Color(0xFFFA622F),
                            fontSize = sp15,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false), fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = stringResource(R.string.product_price, product.discountedPrice ?: 0),
                            fontSize = sp15,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false), fontWeight = FontWeight.Bold)
                        )
                    }
                    Text(
                        text = stringResource(R.string.product_price, product.originalPrice ?: 0),
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = sp13,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }
            }
        } else {
            Text(
                text = stringResource(R.string.product_price, product.originalPrice ?: 0),
                fontSize = sp15,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false), fontWeight = FontWeight.Bold)
            )
        }
    }
}