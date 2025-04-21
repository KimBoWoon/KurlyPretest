package com.bowoon.network

import com.bowoon.network.model.NetworkProducts
import com.bowoon.network.model.NetworkSectionInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface KurlyApis {
    @GET("/sections")
    suspend fun getSections(
        @Query("page") page: Int
    ): ApiResponse<NetworkSectionInfo>

    @GET("/section/products")
    suspend fun getProducts(
        @Query("sectionId") sectionId: Int
    ): ApiResponse<NetworkProducts>
}