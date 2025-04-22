package com.bowoon.model

data class MainSection(
    val sectionId: Int? = null,
    val type: String? = null,
    val title: String? = null,
    val products: List<Product>? = null
)