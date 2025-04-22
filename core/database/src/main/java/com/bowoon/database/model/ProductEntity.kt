package com.bowoon.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bowoon.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: Int?,
    val name: String?,
    val image: String?,
    val originalPrice: Int?,
    val discountedPrice: Int?,
    val isSoldOut: Boolean?,
    val timestamp: Long?
)

fun ProductEntity.asExternalModel(): Product = Product(
    id = id,
    name = name,
    image = image,
    originalPrice = originalPrice,
    discountedPrice = discountedPrice,
    isSoldOut = isSoldOut
)