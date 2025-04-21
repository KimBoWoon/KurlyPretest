package com.bowoon.model

data class SectionInfo(
    val data: List<Section>? = null,
    val paging: Paging? = null
)

data class Section(
    val title: String? = null,
    val id: Int? = null,
    val type: String? = null,
    val url: String? = null,
    var products: Products? = null
)

data class Paging(
    val nextPage: Int? = null
)