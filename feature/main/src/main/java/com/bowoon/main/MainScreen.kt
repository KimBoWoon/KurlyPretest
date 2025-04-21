package com.bowoon.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bowoon.common.Log
import com.bowoon.model.Product
import com.bowoon.ui.image.DynamicAsyncImageLoader
import com.bowoon.ui.utils.dp1
import com.bowoon.ui.utils.dp10
import com.bowoon.ui.utils.dp150
import com.bowoon.ui.utils.dp16
import com.bowoon.ui.utils.dp20
import com.bowoon.ui.utils.dp200
import com.bowoon.ui.utils.sp20
import java.util.Locale

@Composable
fun MainScreen(
    mainVM: MainVM = hiltViewModel()
) {
    val sectionPager = mainVM.sectionPager.collectAsLazyPagingItems()

    MainScreen(
        sectionPager = sectionPager
    )
}

@Composable
fun MainScreen(
    sectionPager: LazyPagingItems<MainUiModel>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = sectionPager.itemCount,
            key = { index ->
                when (val section = sectionPager.peek(index)) {
                    is MainUiModel.Separator -> "$index separator"
                    is MainUiModel.Data -> "${section.mainProduct.sectionId}_${section.mainProduct.title}"
                    null -> {}
                }
            }
        ) { index ->
            Column {
                when (val item = sectionPager[index]) {
                    is MainUiModel.Separator -> Spacer(modifier = Modifier.padding(all = dp20).fillMaxWidth().height(height = dp1).background(color = MaterialTheme.colorScheme.primary))
                    is MainUiModel.Data -> {
                        item.mainProduct.let { section ->
                            Text(
                                modifier = Modifier.fillMaxWidth().padding(vertical = dp10),
                                text = section.title ?: "",
                                fontSize = sp20,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                            when (section.type) {
                                "vertical" -> VerticalProductList(section.products ?: emptyList())
                                "horizontal" -> HorizontalProductList(section.products ?: emptyList())
                                "grid" -> GridProductList(section.products ?: emptyList())
                                else -> Log.e("${section.type} is not supported...")
                            }
                        }
                    }
                    null -> {}
                }
            }
        }
    }
}

@Composable
fun VerticalProductList(
    product: List<Product>
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = dp16),
        verticalArrangement = Arrangement.spacedBy(space = dp10)
    ) {
        product.forEach { product ->
            Column {
                DynamicAsyncImageLoader(
                    source = product.image ?: "",
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().aspectRatio(6f / 4f)
                )
                Text(
                    text = product.name ?: "",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (product.discountedPrice != null) {
                    val discountRate = ((product.originalPrice?.toFloat() ?: 0f) - (product.discountedPrice?.toFloat() ?: 0f)) / (product.originalPrice?.toFloat() ?: 0f)
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = String.format(locale = Locale.getDefault(), format = "%.0f%s", discountRate * 100, "%"),
                            color = Color(0xFFFA622F)
                        )
                        Text(
                            text = "${product.discountedPrice}원",
                        )
                        Text(
                            text = "${product.originalPrice}원",
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                } else {
                    Text(
                        text = "${product.originalPrice}원"
                    )
                }
            }
        }
    }
//    LazyColumn(
//
//    ) {
//        items(
//            count = product.size,
//            key = { index -> "${product[index].name}_${product[index].id}" }
//        ) { index ->
//            Column {
//                DynamicAsyncImageLoader(
//                    source = product[index].image ?: "",
//                    contentDescription = null,
//                    modifier = Modifier.fillMaxWidth().aspectRatio(6f / 4f)
//                )
//                Text(
//                    text = product[index].name ?: "",
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//                if (product[index].discountedPrice != null) {
//                    val discountRate = (((product[index].originalPrice?.toFloat() ?: 0f) / (product[index].discountedPrice?.toFloat() ?: 0f)) / (product[index].originalPrice?.toFloat() ?: 0f)) * 100
//                    Row(
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(
//                            text = String.format(locale = Locale.getDefault(), format = "%.2f", discountRate),
//                            color = Color(0xFFFA622F)
//                        )
//                        Text(
//                            text = product[index].discountedPrice.toString(),
//                        )
//                        Text(
//                            text = product[index].originalPrice.toString(),
//                            textDecoration = TextDecoration.LineThrough
//                        )
//                    }
//                } else {
//                    Text(
//                        text = product[index].originalPrice.toString(),
//                    )
//                }
//            }
//        }
//    }
}

@Composable
fun HorizontalProductList(
    product: List<Product>
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(space = dp10),
        contentPadding = PaddingValues(horizontal = dp16)
    ) {
        items(
            count = product.size,
            key = { index -> "${product[index].name}_${product[index].id}" }
        ) { index ->
            Column(
                modifier = Modifier.width(width = dp150)
            ) {
                DynamicAsyncImageLoader(
                    source = product[index].image ?: "",
                    contentDescription = null,
                    modifier = Modifier.size(width = dp150, height = dp200)
                )
                Text(
                    text = product[index].name ?: "",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (product[index].discountedPrice != null) {
                    val discountRate = ((product[index].originalPrice?.toFloat() ?: 0f) - (product[index].discountedPrice?.toFloat() ?: 0f)) / (product[index].originalPrice?.toFloat() ?: 0f)
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = String.format(locale = Locale.getDefault(), format = "%.0f%s", discountRate * 100, "%"),
                            color = Color(0xFFFA622F)
                        )
                        Text(
                            text = "${product[index].discountedPrice}원",
                        )
                    }
                    Text(
                        text = "${product[index].originalPrice}원",
                        textDecoration = TextDecoration.LineThrough
                    )
                } else {
                    Text(
                        text = "${product[index].originalPrice}원"
                    )
                }
            }
        }
    }
}

@Composable
fun GridProductList(
    product: List<Product>
) {
    LazyVerticalGrid(
        modifier = Modifier.heightIn(max = 1000.dp),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = dp16),
        horizontalArrangement = Arrangement.spacedBy(space = dp10),
        verticalArrangement = Arrangement.spacedBy(space = dp10)
    ) {
        items(
            count = product.size,
            key = { index -> "${product[index].name}_${product[index].id}" }
        ) { index ->
            Column {
                DynamicAsyncImageLoader(
                    source = product[index].image ?: "",
                    contentDescription = null,
                    modifier = Modifier.size(width = dp150, height = dp200)
                )
                Text(
                    text = product[index].name ?: "",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (product[index].discountedPrice != null) {
                    val discountRate = ((product[index].originalPrice?.toFloat() ?: 0f) - (product[index].discountedPrice?.toFloat() ?: 0f)) / (product[index].originalPrice?.toFloat() ?: 0f)
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = String.format(locale = Locale.getDefault(), format = "%.0f%s", discountRate * 100, "%"),
                            color = Color(0xFFFA622F)
                        )
                        Text(
                            text = "${product[index].discountedPrice}원"
                        )
                    }
                    Text(
                        text = "${product[index].originalPrice}원",
                        textDecoration = TextDecoration.LineThrough
                    )
                } else {
                    Text(
                        text = "${product[index].originalPrice}원"
                    )
                }
            }
        }
    }
}

enum class SectionType(val label: String) {
    VERTICAL("vertical"), HORIZONTAL("horizontal"), GRID("grid")
}