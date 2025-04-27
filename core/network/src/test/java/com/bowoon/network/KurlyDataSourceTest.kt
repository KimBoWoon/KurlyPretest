package com.bowoon.network

import com.bowoon.testing.TestKurlyDataSource
import com.bowoon.testing.model.testSectionInfo
import com.bowoon.testing.utils.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class KurlyDataSourceTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val datasource = TestKurlyDataSource()

    @Test
    fun getSectionsTest() = runTest {
        val result = datasource.getSections(1)

        assertEquals(result, testSectionInfo)
    }

    @Test
    fun getProductsTest() = runTest {
        testSectionInfo.data?.forEachIndexed { index, section ->
            val result = datasource.getProducts(index)
            assertEquals(result, section.products)
        }
    }
}