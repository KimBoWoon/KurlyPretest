package com.bowoon.mockserver_test

import com.kurly.android.mockserver.core.FileProvider
import com.kurly.android.mockserver.core.TestAssetFileProvider
import com.kurly.android.mockserver.di.FileProviderModules
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FileProviderModules::class]
)
abstract class TestFileProviderModules {
    @Binds
    abstract fun bindFileProvider(
        fileProvider: TestAssetFileProvider
    ): FileProvider
}