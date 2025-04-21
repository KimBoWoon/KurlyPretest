package com.bowoon.data.repository

import com.bowoon.data.paging.KurlySectionPaging
import com.bowoon.model.Products
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getSection(): KurlySectionPaging
    fun getProducts(sectionId: Int): Flow<Products>
}