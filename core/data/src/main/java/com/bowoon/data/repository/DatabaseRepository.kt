package com.bowoon.data.repository

import com.bowoon.model.Product
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun insertProduct(product: Product): Long
    suspend fun deleteProduct(product: Product)
}