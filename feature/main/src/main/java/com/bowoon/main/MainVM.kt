package com.bowoon.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.bowoon.common.Log
import com.bowoon.data.repository.DatabaseRepository
import com.bowoon.data.repository.MainRepository
import com.bowoon.model.MainSection
import com.bowoon.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val mainRepository: MainRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {
    companion object {
        private const val TAG = "MainVM"
    }

    val sectionPager = combine(
        Pager(
            config = PagingConfig(pageSize = 1, prefetchDistance = 5, initialLoadSize = 1),
            initialKey = 1,
            pagingSourceFactory = { mainRepository.getSection() }
        ).flow.cachedIn(scope = viewModelScope).map {
            it.map { mainSection -> MainUiModel.Data(mainSection) }
                .insertSeparators { before: MainUiModel.Data?, after: MainUiModel.Data? ->
                    if (before != null && after != null) {
                        MainUiModel.Separator
                    } else {
                        null
                    }
                }
        },
        databaseRepository.getProducts()
    ) { pager, favoriteList ->
        pager.map {
            when (it) {
                is MainUiModel.Data -> {
                    MainUiModel.Data(
                        MainSection(
                            sectionId = it.mainProduct.sectionId,
                            type = it.mainProduct.type,
                            title = it.mainProduct.title,
                            products = it.mainProduct.products?.map { product ->
                                product.copy(isFavorite = favoriteList.find { it.id == product.id } != null)
                            }
                        )
                    )
                }
                else -> it
            }
        }
    }

    fun addFavorite(product: Product) {
        viewModelScope.launch {
            Log.d(TAG, "addFavorite")
            databaseRepository.insertProduct(product)
        }
    }

    fun removeFavorite(product: Product) {
        viewModelScope.launch {
            Log.d(TAG, "removeFavorite")
            databaseRepository.deleteProduct(product)
        }
    }
}

sealed interface MainUiModel {
    data object Separator : MainUiModel
    data class Data(val mainProduct: MainSection) : MainUiModel
}