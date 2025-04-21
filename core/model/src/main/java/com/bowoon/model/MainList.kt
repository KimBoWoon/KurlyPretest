package com.bowoon.model

data class MainSection(
    val data: List<MainProduct>? = null
)

data class MainProduct(
    val sectionId: Int? = null,
    val type: String? = null,
    val title: String? = null,
    val products: List<Product>? = null
)