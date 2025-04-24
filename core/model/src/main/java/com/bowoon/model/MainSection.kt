package com.bowoon.model

data class MainSection(
    val sectionId: Int? = null,
    val type: SectionType = SectionType.NONE,
    val title: String? = null,
    val products: List<Product>? = null
)