package com.bowoon.database.di

import com.bowoon.database.ProductDatabase
import com.bowoon.database.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesProductDao(
        database: ProductDatabase,
    ): ProductDao = database.productDao()
}
