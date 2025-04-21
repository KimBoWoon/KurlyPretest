package com.bowoon.model

data class Products(
    val data: List<Product>? = null
)

data class Product(
    val id: Int? = null,
    val name: String? = null,
    val image: String? = null,
    val originalPrice: Int? = null,
    val discountedPrice: Int? = null,
    val isSoldOut: Boolean? = null,
)