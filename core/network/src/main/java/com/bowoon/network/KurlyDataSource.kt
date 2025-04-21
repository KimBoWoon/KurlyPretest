package com.bowoon.network

import com.bowoon.model.Products
import com.bowoon.model.SectionInfo

interface KurlyDataSource {
    suspend fun getSections(
        page: Int
    ): SectionInfo

    suspend fun getProducts(
        sectionId: Int
    ): Products
}