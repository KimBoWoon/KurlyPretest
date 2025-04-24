package com.bowoon.testing.repository

import com.bowoon.data.paging.KurlySectionPaging
import com.bowoon.data.repository.SectionRepository
import com.bowoon.model.Products
import com.bowoon.testing.TestKurlyDataSource
import com.bowoon.testing.model.testSectionInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestSectionRepository : SectionRepository {
    override fun getSection(): KurlySectionPaging =
        KurlySectionPaging(
            apis = TestKurlyDataSource()
        )

    override fun getProducts(sectionId: Int): Flow<Products> = flow {
        emit(testSectionInfo.data?.get(sectionId)?.products ?: Products())
    }
}