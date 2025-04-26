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
//    var errorFlag = false
//    private val items = if (errorFlag) {
//        testSectionInfo.data?.map { section ->
//            MainSection(
//                sectionId = section.id,
//                type = SectionType.entries.find { it.label == section.type } ?: SectionType.NONE,
//                title = section.title,
//                products = section.products?.data
//            )
//        } ?: emptyList()
//    } else {
//        throw RuntimeException("error test")
//    }
//    @SuppressLint("VisibleForTests")
//    private val pagingSourceFactory = items.asPagingSourceFactory()
//    val pagingSource = pagingSourceFactory()

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