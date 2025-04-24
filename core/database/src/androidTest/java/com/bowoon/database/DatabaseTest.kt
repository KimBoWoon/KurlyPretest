package com.bowoon.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.bowoon.database.dao.ProductDao
import org.junit.After
import org.junit.Before

internal abstract class DatabaseTest {
    private lateinit var db: ProductDatabase
    protected lateinit var productDao: ProductDao

    @Before
    fun setup() {
        db = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(
                context,
                ProductDatabase::class.java,
            ).build()
        }
        productDao = db.productDao()
    }

    @After
    fun teardown() = db.close()
}
