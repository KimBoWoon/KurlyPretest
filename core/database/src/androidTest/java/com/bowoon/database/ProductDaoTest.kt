package com.bowoon.database

import com.bowoon.database.model.ProductEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.threeten.bp.Instant
import kotlin.test.assertEquals

internal class ProductDaoTest : DatabaseTest() {
    private val favoriteProducts = listOf(
        ProductEntity(
            id = 1,
            name = "productName1",
            image = "/productImageUrl1.png",
            originalPrice = 2000,
            discountedPrice = 1500,
            isSoldOut = false,
            timestamp = Instant.now().epochSecond,
        ),
        ProductEntity(
            id = 2,
            name = "productName2",
            image = "/productImageUrl2.jpg",
            originalPrice = 2400,
            discountedPrice = 1800,
            isSoldOut = false,
            timestamp = Instant.now().epochSecond,
        ),
        ProductEntity(
            id = 3,
            name = "productName3",
            image = "/productImageUrl3.png",
            originalPrice = 5900,
            discountedPrice = 3800,
            isSoldOut = false,
            timestamp = Instant.now().epochSecond,
        )
    )

    @Test
    fun getProductTest() = runTest {
        assertEquals(
            productDao.getProductEntities().first(),
            emptyList()
        )

        favoriteProducts.forEach {
            productDao.insertOrIgnoreProducts(it)
        }

        assertEquals(
            productDao.getProductEntities().first(),
            favoriteProducts
        )
    }

    @Test
    fun deleteProductTest() = runTest {
        assertEquals(
            productDao.getProductEntities().first(),
            emptyList()
        )

        favoriteProducts.forEach {
            productDao.insertOrIgnoreProducts(it)
        }

        assertEquals(
            productDao.getProductEntities().first(),
            favoriteProducts
        )

        productDao.deleteProduct(2)

        assertEquals(
            productDao.getProductEntities().first(),
            favoriteProducts.filter { it.id != 2 }
        )
    }

    @Test
    fun insertOrIgnoreTest() = runTest {
        val product = ProductEntity(
            id = 4,
            name = "productName4",
            image = "/productImageUrl.png",
            originalPrice = 4980,
            discountedPrice = null,
            isSoldOut = false,
            timestamp = Instant.now().epochSecond
        )

        favoriteProducts.forEach {
            productDao.insertOrIgnoreProducts(it)
        }

        assertEquals(
            productDao.getProductEntities().first(),
            favoriteProducts
        )

        productDao.insertOrIgnoreProducts(product)

        assertEquals(
            productDao.getProductEntities().first(),
            favoriteProducts + product
        )
    }
}