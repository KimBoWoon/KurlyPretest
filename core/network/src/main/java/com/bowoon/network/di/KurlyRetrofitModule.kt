package com.bowoon.network.di

import com.bowoon.kurlypretest.core.network.BuildConfig
import com.bowoon.network.utils.NetworkLogInterceptor
import com.kurly.android.mockserver.MockInterceptor
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object KurlyRetrofitModule {
    @Provides
    fun provideKurlyOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        okHttpProfilerInterceptor: OkHttpProfilerInterceptor,
        networkLogInterceptor: NetworkLogInterceptor,
        mockInterceptor: MockInterceptor
    ): OkHttpClient = OkHttpClient().newBuilder().apply {
        connectTimeout(1, TimeUnit.MINUTES)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(15, TimeUnit.SECONDS)
        addNetworkInterceptor(httpLoggingInterceptor)
        addInterceptor(mockInterceptor)
        if (BuildConfig.IS_DEBUGGING_LOGGING) {
            addInterceptor(okHttpProfilerInterceptor)
            addInterceptor(networkLogInterceptor)
        }
    }.build()

    @Provides
    fun provideKotlinSerialization(): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Provides
    fun provideJsonMediaType(): MediaType = "application/json".toMediaType()

    @Provides
    fun provideInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.IS_DEBUGGING_LOGGING) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    fun provideOkHttpProfilerInterceptor(): OkHttpProfilerInterceptor = OkHttpProfilerInterceptor()

    @Provides
    fun provideKurlyUrl(): String = "https://kurly.com"
}