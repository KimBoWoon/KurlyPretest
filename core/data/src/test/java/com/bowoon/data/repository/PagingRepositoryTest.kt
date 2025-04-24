package com.bowoon.data.repository

import androidx.paging.PagingSource
import com.bowoon.data.paging.KurlySectionPaging
import com.bowoon.model.MainSection
import com.bowoon.model.SectionType
import com.bowoon.testing.TestKurlyDataSource
import com.bowoon.testing.model.testSectionInfo
import com.bowoon.testing.utils.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class PagingRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testKurlyApi = TestKurlyDataSource()

    @Test
    fun sectionPagingTest() = runTest {
        val pagingSource = KurlySectionPaging(
            apis = testKurlyApi
        )

        val a: PagingSource.LoadResult<Int, MainSection> = PagingSource.LoadResult.Page(
            data = testSectionInfo.data?.map { section ->
                MainSection(
                    sectionId = section.id,
                    type = SectionType.entries.find { it.label == section.type } ?: SectionType.NONE,
                    title = section.title,
                    products = testSectionInfo.data?.get(section.id)?.products?.data
                )
            } ?: emptyList(),
            prevKey = null,
            nextKey = testSectionInfo.paging?.nextPage
        )
        val b = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        assertEquals(
            expected = a,
            actual = b,
        )
    }
}