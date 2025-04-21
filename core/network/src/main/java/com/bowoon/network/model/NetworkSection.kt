package com.bowoon.network.model

import com.bowoon.model.Paging
import com.bowoon.model.Section
import com.bowoon.model.SectionInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkSectionInfo(
    @SerialName("data")
    val data: List<NetworkSection>? = null,
    @SerialName("paging")
    val paging: NetworkPaging? = null
)

@Serializable
data class NetworkSection(
    @SerialName("title")
    val title: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("url")
    val url: String? = null
)

@Serializable
data class NetworkPaging(
    @SerialName("next_page")
    val nextPage: Int? = null
)

fun NetworkSectionInfo.asExternalModel(): SectionInfo =
    SectionInfo(
        data = data?.asExternalModel(),
        paging = paging?.asExternalModel()
    )

fun List<NetworkSection>.asExternalModel(): List<Section> =
    map {
        Section(
            title = it.title,
            id = it.id,
            type = it.type,
            url = it.url
        )
    }

fun NetworkPaging.asExternalModel(): Paging =
    Paging(
        nextPage = nextPage
    )