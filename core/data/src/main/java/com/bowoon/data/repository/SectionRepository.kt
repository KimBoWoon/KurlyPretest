package com.bowoon.data.repository

import com.bowoon.data.paging.SectionPaging
import com.bowoon.model.Products
import kotlinx.coroutines.flow.Flow

interface SectionRepository {
    fun getKurlyPagingSource(): SectionPaging
    fun getProducts(sectionId: Int): Flow<Products>
}