package com.bowoon.testing.repository

import com.bowoon.data.repository.DatabaseRepository
import com.bowoon.model.Product
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestDatabaseRepository : DatabaseRepository {
    val productDatabase = MutableSharedFlow<List<Product>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val currentProductDatabase get() = productDatabase.replayCache.firstOrNull() ?: emptyList()

    override fun getProducts(): Flow<List<Product>> = productDatabase

    override suspend fun insertProduct(product: Product): Long {
        productDatabase.emit(currentProductDatabase + product)
        return product.id?.toLong() ?: -1L
    }

    override suspend fun deleteProduct(product: Product) {
        productDatabase.emit(currentProductDatabase.filter { it.id != product.id })
    }
}