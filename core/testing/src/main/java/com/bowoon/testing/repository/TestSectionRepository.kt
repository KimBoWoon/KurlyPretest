package com.bowoon.testing.repository

import com.bowoon.data.paging.KurlySectionPaging
import com.bowoon.data.repository.SectionRepository
import com.bowoon.model.Products
import com.bowoon.testing.TestKurlyDataSource
import com.bowoon.testing.model.testSectionInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestSectionRepository : SectionRepository {
    private val testKurlySectionPaging = TestKurlyDataSource()

    override fun getKurlyPagingSource(): KurlySectionPaging =
        KurlySectionPaging(
            apis = testKurlySectionPaging
        )

    override fun getProducts(sectionId: Int): Flow<Products> = flow {
        emit(testSectionInfo.data?.get(sectionId)?.products ?: Products())
    }

    fun setErrorFlag(value: Boolean) {
        testKurlySectionPaging.errorFlag = value
    }
}