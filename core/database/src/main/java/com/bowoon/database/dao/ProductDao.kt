package com.bowoon.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bowoon.database.model.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query(value = "SELECT * FROM products")
    fun getProductEntities(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreProducts(product: ProductEntity): Long

    @Query(value = "DELETE FROM products WHERE id = (:id)")
    suspend fun deleteProduct(id: Int)
}