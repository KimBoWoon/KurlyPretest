package com.bowoon.testing

import com.bowoon.model.Products
import com.bowoon.model.SectionInfo
import com.bowoon.network.KurlyDataSource
import com.bowoon.testing.model.testSectionInfo

class TestKurlyDataSource : KurlyDataSource {
    override suspend fun getSections(page: Int): SectionInfo = testSectionInfo
    override suspend fun getProducts(sectionId: Int): Products = testSectionInfo.data?.get(sectionId)?.products ?: Products()
}