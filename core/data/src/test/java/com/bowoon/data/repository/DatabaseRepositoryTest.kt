package com.bowoon.data.repository

import com.bowoon.data.testdouble.TestProductDao
import com.bowoon.model.Product
import com.bowoon.testing.utils.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class DatabaseRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val repository = DatabaseRepositoryImpl(
        productDao = TestProductDao()
    )

    @Test
    fun insertProductTest() = runTest {
        val product = Product(
            id = 1,
            name = "productName1",
            image = "/productImageUrl1.png",
            originalPrice = 3800,
            discountedPrice = 2000,
            isSoldOut = false,
            isFavorite = false
        )

        assertEquals(
            repository.getProducts().first(),
            emptyList()
        )

        repository.insertProduct(product)

        assertEquals(
            repository.getProducts().first(),
            listOf(product)
        )
    }

    @Test
    fun deleteProductTest() = runTest {
        val product = Product(
            id = 1,
            name = "productName1",
            image = "/productImageUrl1.png",
            originalPrice = 3800,
            discountedPrice = 2000,
            isSoldOut = false,
            isFavorite = false
        )

        assertEquals(
            repository.getProducts().first(),
            emptyList()
        )

        repository.insertProduct(product)

        assertEquals(
            repository.getProducts().first(),
            listOf(product)
        )

        repository.deleteProduct(product)

        assertEquals(
            repository.getProducts().first(),
            emptyList()
        )
    }
}