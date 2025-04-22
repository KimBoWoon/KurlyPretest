package com.bowoon.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bowoon.database.dao.ProductDao
import com.bowoon.database.model.ProductEntity

@Database(
    entities = [
        ProductEntity::class
    ],
    version = 1,
    autoMigrations = [],
    exportSchema = true,
)
internal abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}