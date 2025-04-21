package com.bowoon.data.repository

import com.bowoon.data.paging.KurlySectionPaging
import com.bowoon.model.Products
import com.bowoon.network.KurlyDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val kurlyDataSource: KurlyDataSource
) : MainRepository {
    override fun getSection(): KurlySectionPaging = KurlySectionPaging(kurlyDataSource)
    override fun getProducts(sectionId: Int): Flow<Products> = flow {
        emit(kurlyDataSource.getProducts(sectionId))
    }
}