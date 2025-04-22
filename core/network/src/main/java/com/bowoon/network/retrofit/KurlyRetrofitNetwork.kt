package com.bowoon.network.retrofit

import com.bowoon.model.Products
import com.bowoon.model.SectionInfo
import com.bowoon.network.ApiResponse
import com.bowoon.network.CustomCallAdapter
import com.bowoon.network.KurlyApis
import com.bowoon.network.KurlyDataSource
import com.bowoon.network.model.asExternalModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 데이터를 가져오는 Api
 * @param retrofit 레트로핏 모듈
 */
@Singleton
class KurlyRetrofitNetwork @Inject constructor(
    baseUrl: String,
    client: OkHttpClient,
    customCallAdapter: CustomCallAdapter,
    serialization: Json,
    jsonMediaType: MediaType
) : KurlyDataSource {
    private val kurlyApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(customCallAdapter)
        .addConverterFactory(serialization.asConverterFactory(jsonMediaType))
        .client(client)
        .build()
        .create(KurlyApis::class.java)

    override suspend fun getSections(page: Int): SectionInfo =
        when (val response = kurlyApi.getSections(page)) {
            is ApiResponse.Failure -> throw response.throwable
            is ApiResponse.Success -> response.data.asExternalModel()
        }

    override suspend fun getProducts(sectionId: Int): Products =
        when (val response = kurlyApi.getProducts(sectionId)) {
            is ApiResponse.Failure -> throw response.throwable
            is ApiResponse.Success -> response.data.asExternalModel()
        }
}