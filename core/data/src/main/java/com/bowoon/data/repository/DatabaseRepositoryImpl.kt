package com.bowoon.data.repository

import com.bowoon.database.dao.ProductDao
import com.bowoon.database.model.ProductEntity
import com.bowoon.database.model.asExternalModel
import com.bowoon.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.Instant
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : DatabaseRepository {
    override fun getProducts(): Flow<List<Product>> =
        productDao.getProductEntities()
            .map {
                it.sortedByDescending { it.timestamp }
                    .map(ProductEntity::asExternalModel)
            }

    override suspend fun insertProduct(product: Product): Long =
        productDao.insertOrIgnoreProducts(
            ProductEntity(
                id = product.id,
                name = product.name,
                image = product.image,
                originalPrice = product.originalPrice,
                discountedPrice = product.discountedPrice,
                isSoldOut = product.isSoldOut,
                timestamp = Instant.now().toEpochMilli()
            )
        )

    override suspend fun deleteProduct(product: Product) {
        product.id?.let {
            productDao.deleteProduct(it)
        }
    }
}