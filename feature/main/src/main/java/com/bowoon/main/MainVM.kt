package com.bowoon.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.bowoon.data.repository.MainRepository
import com.bowoon.model.MainProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    companion object {
        private const val TAG = "MainVM"
    }

    val sectionPager = Pager(
        config = PagingConfig(pageSize = 1, prefetchDistance = 5, initialLoadSize = 1),
        initialKey = 1,
        pagingSourceFactory = { mainRepository.getSection() }
    ).flow.cachedIn(scope = viewModelScope)
        .map { pagingData -> pagingData.map { MainUiModel.Data(it) } }
        .map {
            it.insertSeparators { before: MainUiModel.Data?, after: MainUiModel.Data? ->
                if (before != null && after != null) {
                    MainUiModel.Separator
                } else {
                    null
                }
            }
    }
}

sealed interface MainUiModel {
    data object Separator : MainUiModel
    data class Data(val mainProduct: MainProduct) : MainUiModel
}