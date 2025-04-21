package com.bowoon.network.model

import com.bowoon.model.Product
import com.bowoon.model.Products
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProducts(
    @SerialName("data")
    val data: List<NetworkProduct>? = null
)

@Serializable
data class NetworkProduct(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("image")
    val image: String? = null,
    @SerialName("originalPrice")
    val originalPrice: Int? = null,
    @SerialName("discountedPrice")
    val discountedPrice: Int? = null,
    @SerialName("isSoldOut")
    val isSoldOut: Boolean? = null,
)

fun NetworkProducts.asExternalModel(): Products =
    Products(data = data?.asExternalModel())

fun List<NetworkProduct>.asExternalModel(): List<Product> =
    map {
        Product(
            id = it.id,
            name = it.name,
            image = it.image,
            originalPrice = it.originalPrice,
            discountedPrice = it.discountedPrice,
            isSoldOut = it.isSoldOut
        )
    }