package com.bowoon.data.di

import com.bowoon.data.repository.DatabaseRepository
import com.bowoon.data.repository.DatabaseRepositoryImpl
import com.bowoon.data.repository.SectionRepository
import com.bowoon.data.repository.SectionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModules {
    @Binds
    abstract fun bindSectionRepository(
        mainRepository: SectionRepositoryImpl
    ): SectionRepository

    @Binds
    abstract fun bindDatabaseRepository(
        databaseRepository: DatabaseRepositoryImpl
    ): DatabaseRepository
}