package com.kurly.android.mockserver.di

import com.kurly.android.mockserver.MockServer
import com.kurly.android.mockserver.core.FileProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MockServerModules {
    @Provides
    fun provideMockServer(
        fileProvider: FileProvider
    ): MockServer = MockServer(fileProvider = fileProvider)
}