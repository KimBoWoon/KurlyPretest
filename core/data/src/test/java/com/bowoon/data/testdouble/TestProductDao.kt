package com.bowoon.data.testdouble

import com.bowoon.database.dao.ProductDao
import com.bowoon.database.model.ProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TestProductDao : ProductDao {
    private val entitiesStateFlow = MutableStateFlow(emptyList<ProductEntity>())

    override fun getProductEntities(): Flow<List<ProductEntity>> = entitiesStateFlow

    override suspend fun insertOrIgnoreProducts(product: ProductEntity): Long {
        entitiesStateFlow.update { oldValue ->
            (oldValue + product).distinctBy(ProductEntity::id)
        }
        return product.id?.toLong() ?: -1
    }

    override suspend fun deleteProduct(id: Int) {
        entitiesStateFlow.update { entities -> entities.filterNot { it.id == id } }
    }
}