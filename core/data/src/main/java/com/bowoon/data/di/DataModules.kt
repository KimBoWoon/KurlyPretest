package com.bowoon.data.di

import com.bowoon.data.repository.MainRepository
import com.bowoon.data.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModules {
    @Binds
    abstract fun bindUserRepository(
        mainRepository: MainRepositoryImpl
    ): MainRepository
}