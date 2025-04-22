package com.bowoon.database.di

import android.content.Context
import androidx.room.Room
import com.bowoon.database.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesProductDatabase(
        @ApplicationContext context: Context
    ): ProductDatabase = Room.databaseBuilder(
        context,
        ProductDatabase::class.java,
        "product-database",
    ).build()
}
