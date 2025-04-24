package com.bowoon.common

fun getDiscountRate(
    originalPrice: Int,
    discountedPrice: Int
): Float = ((originalPrice.toFloat() - discountedPrice.toFloat()) / originalPrice.toFloat()) * 100