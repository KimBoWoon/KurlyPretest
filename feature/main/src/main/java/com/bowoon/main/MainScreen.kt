package com.bowoon.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bowoon.common.Log
import com.bowoon.model.Product
import com.bowoon.ui.components.FavoriteButtonComponent
import com.bowoon.ui.components.PagingAppendErrorComponent
import com.bowoon.ui.dialog.ConfirmDialog
import com.bowoon.ui.image.DynamicAsyncImageLoader
import com.bowoon.ui.utils.dp1
import com.bowoon.ui.utils.dp10
import com.bowoon.ui.utils.dp150
import com.bowoon.ui.utils.dp20
import com.bowoon.ui.utils.dp200
import com.bowoon.ui.utils.dp5
import com.bowoon.ui.utils.sp13
import com.bowoon.ui.utils.sp15
import com.bowoon.ui.utils.sp20
import java.util.Locale

@Composable
fun MainScreen(
    viewModel: MainVM = hiltViewModel()
) {
    val sectionPager = viewModel.sectionPager.collectAsLazyPagingItems()

    MainScreen(
        sectionPager = sectionPager,
        addFavorite = viewModel::addFavorite,
        removeFavorite = viewModel::removeFavorite
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    sectionPager: LazyPagingItems<MainUiModel>,
    addFavorite: (Product) -> Unit,
    removeFavorite: (Product) -> Unit
) {
    var isAppend by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = { sectionPager.refresh() }
    ) {
        when {
            sectionPager.loadState.refresh is LoadState.Loading -> {
                isRefreshing = true
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            sectionPager.loadState.append is LoadState.Loading -> isAppend = true
            sectionPager.loadState.refresh is LoadState.NotLoading -> isRefreshing = false
            sectionPager.loadState.refresh is LoadState.Error -> {
                ConfirmDialog(
                    title = "통신 실패",
                    message = (sectionPager.loadState.refresh as? LoadState.Error)?.error?.message ?: "something wrong...",
                    confirmPair = "재시도" to { sectionPager.retry() },
                    dismissPair = "취소" to {}
                )
            }
            sectionPager.loadState.append is LoadState.NotLoading -> isAppend = false
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
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
                                    "vertical" -> VerticalProductList(
                                        product = section.products ?: emptyList(),
                                        addFavorite = addFavorite,
                                        removeFavorite = removeFavorite
                                    )
                                    "horizontal" -> HorizontalProductList(
                                        product = section.products ?: emptyList(),
                                        addFavorite = addFavorite,
                                        removeFavorite = removeFavorite
                                    )
                                    "grid" -> GridProductList(
                                        product = section.products?.take(6) ?: emptyList(),
                                        addFavorite = addFavorite,
                                        removeFavorite = removeFavorite
                                    )
                                    else -> Log.e("${section.type} is not supported...")
                                }
                            }
                        }
                        null -> {}
                    }
                }
            }
            if (isAppend) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.wrapContentSize().padding(vertical = dp20)
                    )
                }
            }
            if (sectionPager.loadState.append is LoadState.Error) {
                item {
                    PagingAppendErrorComponent { sectionPager.retry() }
                }
            }
        }
    }
}

@Composable
fun VerticalProductList(
    product: List<Product>,
    addFavorite: (Product) -> Unit,
    removeFavorite: (Product) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = dp10),
        verticalArrangement = Arrangement.spacedBy(space = dp10)
    ) {
        product.forEach { product ->
            VerticalProductComponent(
                product = product,
                addFavorite = addFavorite,
                removeFavorite = removeFavorite
            )
        }
    }
}

@Composable
fun HorizontalProductList(
    product: List<Product>,
    addFavorite: (Product) -> Unit,
    removeFavorite: (Product) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(space = dp10),
        contentPadding = PaddingValues(horizontal = dp10)
    ) {
        items(
            count = product.size,
            key = { index -> "${product[index].name}_${product[index].id}" }
        ) { index ->
            ProductComponent(
                product = product[index],
                addFavorite = addFavorite,
                removeFavorite = removeFavorite
            )
        }
    }
}

@Composable
fun GridProductList(
    product: List<Product>,
    addFavorite: (Product) -> Unit,
    removeFavorite: (Product) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.heightIn(max = 1000.dp),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = dp10),
        horizontalArrangement = Arrangement.spacedBy(space = dp10),
        verticalArrangement = Arrangement.spacedBy(space = dp10)
    ) {
        items(
            count = product.size,
            key = { index -> "${product[index].name}_${product[index].id}" }
        ) { index ->
            ProductComponent(
                product = product[index],
                addFavorite = addFavorite,
                removeFavorite = removeFavorite
            )
        }
    }
}

@Composable
fun VerticalProductComponent(
    product: Product,
    addFavorite: (Product) -> Unit,
    removeFavorite: (Product) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ProductImageComponent(
            modifier = Modifier.fillMaxWidth().aspectRatio(6f / 4f),
            source = product.image ?: "",
            isFavorite = product.isFavorite,
            addFavorite = { addFavorite(product) },
            removeFavorite = { removeFavorite(product) }
        )
        ProductTitleComponent(
            title = product.name ?: "",
            titleLine = 1
        )
        if (product.discountedPrice != null) {
            val discountRate = getDiscountRate(product.originalPrice ?: 0, product.discountedPrice ?: 0)

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = dp5),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format(locale = Locale.getDefault(), format = "%.0f%s", discountRate, "%"),
                    color = Color(0xFFFA622F),
                    fontSize = sp15,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false), fontWeight = FontWeight.Bold)
                )
                Text(
                    modifier = Modifier.padding(horizontal = dp5),
                    text = "${product.discountedPrice}원",
                    fontSize = sp15,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false), fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${product.originalPrice}원",
                    textDecoration = TextDecoration.LineThrough,
                    fontSize = sp13,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }
        } else {
            Text(
                text = "${product.originalPrice}원",
                fontSize = sp15,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false), fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ProductComponent(
    product: Product,
    addFavorite: (Product) -> Unit,
    removeFavorite: (Product) -> Unit
) {
    Column(
        modifier = Modifier.width(width = dp150).wrapContentHeight()
    ) {
        ProductImageComponent(
            modifier = Modifier.fillMaxWidth().height(height = dp200),
            source = product.image ?: "",
            isFavorite = product.isFavorite,
            addFavorite = { addFavorite(product) },
            removeFavorite = { removeFavorite(product) }
        )
        ProductTitleComponent(
            title = product.name ?: "",
            titleLine = 2
        )
        if (product.discountedPrice != null) {
            val discountRate = getDiscountRate(product.originalPrice ?: 0, product.discountedPrice ?: 0)

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = dp5)
            ) {
                Text(
                    modifier = Modifier.padding(end = dp5),
                    text = String.format(locale = Locale.getDefault(), format = "%.0f%s", discountRate, "%"),
                    color = Color(0xFFFA622F),
                    fontSize = sp15,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false), fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${product.discountedPrice}원",
                    fontSize = sp15,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false), fontWeight = FontWeight.Bold)
                )
            }
            Text(
                text = "${product.originalPrice}원",
                textDecoration = TextDecoration.LineThrough,
                fontSize = sp13,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        } else {
            Text(
                text = "${product.originalPrice}원",
                fontSize = sp15,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false), fontWeight = FontWeight.Bold)
            )
            Text(text = "")
        }
    }
}

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

fun getDiscountRate(
    originalPrice: Int,
    discountedPrice: Int
): Float = ((originalPrice.toFloat() - discountedPrice.toFloat()) / originalPrice.toFloat()) * 100