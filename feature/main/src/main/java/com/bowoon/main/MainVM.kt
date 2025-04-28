package com.bowoon.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.bowoon.data.repository.DatabaseRepository
import com.bowoon.data.repository.SectionRepository
import com.bowoon.model.MainSection
import com.bowoon.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val sectionRepository: SectionRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {
    companion object {
        private const val TAG = "MainVM"
    }

    val sectionPager = combine(
        Pager(
            config = PagingConfig(pageSize = 1, prefetchDistance = 3, initialLoadSize = 1),
            initialKey = 1,
            pagingSourceFactory = { sectionRepository.getSectionPagingSource() }
        ).flow.cachedIn(scope = viewModelScope),
        databaseRepository.getProducts()
    ) { pagingData, favoriteList ->
        pagingData.map { mainSection ->
            MainUiModel.Section(
                section = MainSection(
                    sectionId = mainSection.sectionId,
                    type = mainSection.type,
                    title = mainSection.title,
                    products = mainSection.products?.map { product -> product.copy(isFavorite = favoriteList.find { it.id == product.id } != null) }
                )
            )
        }.insertSeparators { before: MainUiModel.Section?, after: MainUiModel.Section? ->
            if (before != null && after != null) {
                MainUiModel.Separator
            } else {
                null
            }
        }
    }

    fun addFavorite(product: Product) {
        viewModelScope.launch {
            databaseRepository.insertProduct(product)
        }
    }

    fun removeFavorite(product: Product) {
        viewModelScope.launch {
            databaseRepository.deleteProduct(product)
        }
    }
}

sealed interface MainUiModel {
    data object Separator : MainUiModel
    data class Section(val section: MainSection) : MainUiModel
}