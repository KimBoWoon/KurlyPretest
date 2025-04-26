package com.bowoon.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bowoon.model.Product
import com.bowoon.model.SectionType
import com.bowoon.ui.components.PagingAppendErrorComponent
import com.bowoon.ui.components.ProductComponent
import com.bowoon.ui.dialog.ConfirmDialog
import com.bowoon.ui.utils.dp1
import com.bowoon.ui.utils.dp10
import com.bowoon.ui.utils.dp1000
import com.bowoon.ui.utils.dp20
import com.bowoon.ui.utils.sp20

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
                CircularProgressIndicator(modifier = Modifier.semantics { contentDescription = "mainLoadingProgress" }.align(Alignment.Center))
            }
            sectionPager.loadState.append is LoadState.Loading -> isAppend = true
            sectionPager.loadState.append is LoadState.Error -> isAppend = false
            sectionPager.loadState.refresh is LoadState.NotLoading -> isRefreshing = false
            sectionPager.loadState.refresh is LoadState.Error -> {
                ConfirmDialog(
                    title = stringResource(com.bowoon.kurlypretest.core.network.R.string.network_connection_failure),
                    message = (sectionPager.loadState.refresh as? LoadState.Error)?.error?.message ?: stringResource(com.bowoon.kurlypretest.core.network.R.string.something_wrong),
                    confirmPair = stringResource(com.bowoon.kurlypretest.core.network.R.string.retry) to { sectionPager.retry() },
                    dismissPair = stringResource(com.bowoon.kurlypretest.core.network.R.string.cancel) to {}
                )
            }
            sectionPager.loadState.append is LoadState.NotLoading -> isAppend = false
        }

        LazyColumn(
            modifier = Modifier.semantics { contentDescription = "sectionList" }.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                count = sectionPager.itemCount,
                key = { index ->
                    when (val section = sectionPager.peek(index)) {
                        is MainUiModel.Separator -> "$index separator"
                        is MainUiModel.Section -> "${section.section.sectionId}_${section.section.title}"
                        null -> {}
                    }
                }
            ) { index ->
                Column {
                    when (val item = sectionPager[index]) {
                        is MainUiModel.Separator -> Spacer(modifier = Modifier.padding(all = dp20).fillMaxWidth().height(height = dp1).background(color = MaterialTheme.colorScheme.primary))
                        is MainUiModel.Section -> {
                            item.section.let { section ->
                                Text(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = dp10),
                                    text = section.title ?: "",
                                    fontSize = sp20,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                                when (section.type) {
                                    SectionType.NONE -> {}
                                    SectionType.VERTICAL -> VerticalProductList(
                                        product = section.products ?: emptyList(),
                                        addFavorite = addFavorite,
                                        removeFavorite = removeFavorite
                                    )
                                    SectionType.HORIZONTAL -> HorizontalProductList(
                                        product = section.products ?: emptyList(),
                                        addFavorite = addFavorite,
                                        removeFavorite = removeFavorite
                                    )
                                    SectionType.GRID -> GridProductList(
                                        product = section.products?.take(6) ?: emptyList(),
                                        addFavorite = addFavorite,
                                        removeFavorite = removeFavorite
                                    )
                                }
                            }
                        }
                        null -> {}
                    }
                }
            }
            if (isAppend && !sectionPager.loadState.append.endOfPaginationReached) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.semantics { contentDescription = "sectionPagingAppend" }.wrapContentSize().padding(vertical = dp20)
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
        modifier = Modifier.semantics { contentDescription = "verticalSectionList" }.fillMaxWidth().padding(horizontal = dp10),
        verticalArrangement = Arrangement.spacedBy(space = dp10)
    ) {
        product.forEach { product ->
            ProductComponent(
                product = product,
                type = SectionType.VERTICAL,
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
        modifier = Modifier.semantics { contentDescription = "horizontalSectionList" }.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = dp10),
        contentPadding = PaddingValues(horizontal = dp10)
    ) {
        items(
            count = product.size,
            key = { index -> "${product[index].name}_${product[index].id}" }
        ) { index ->
            ProductComponent(
                product = product[index],
                type = SectionType.HORIZONTAL,
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
        modifier = Modifier.semantics { contentDescription = "gridSectionList" }.heightIn(max = dp1000),
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
                type = SectionType.GRID,
                addFavorite = addFavorite,
                removeFavorite = removeFavorite
            )
        }
    }
}