package com.kurly.android.mockserver.di

import com.kurly.android.mockserver.core.AssetFileProvider
import com.kurly.android.mockserver.core.FileProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FileProviderModules {
    @Binds
    abstract fun bindFileProvider(
        fileProvider: AssetFileProvider
    ): FileProvider
}