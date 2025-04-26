package com.bowoon.testing

import com.bowoon.model.Products
import com.bowoon.model.SectionInfo
import com.bowoon.network.KurlyDataSource
import com.bowoon.testing.model.testSectionInfo

class TestKurlyDataSource : KurlyDataSource {
    var errorFlag = false

    override suspend fun getSections(page: Int): SectionInfo = if (!errorFlag) testSectionInfo else throw RuntimeException("error test")
    override suspend fun getProducts(sectionId: Int): Products = testSectionInfo.data?.get(sectionId)?.products ?: Products()
}