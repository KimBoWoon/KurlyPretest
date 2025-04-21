package com.bowoon.network.di

import com.bowoon.network.KurlyDataSource
import com.bowoon.network.retrofit.KurlyRetrofitNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModules {
    @Binds
    abstract fun bindApis(apis: KurlyRetrofitNetwork): KurlyDataSource
}