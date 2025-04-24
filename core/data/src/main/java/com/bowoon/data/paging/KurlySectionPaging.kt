package com.bowoon.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bowoon.common.Log
import com.bowoon.model.MainSection
import com.bowoon.model.Products
import com.bowoon.model.SectionType
import com.bowoon.network.KurlyDataSource
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class KurlySectionPaging @Inject constructor(
    private val apis: KurlyDataSource
) : PagingSource<Int, MainSection>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MainSection> =
        runCatching {
            val request = mutableListOf<Deferred<Products>>()
            val response = apis.getSections(params.key ?: 1)
            coroutineScope {
                response.data?.forEach { section ->
                    section.id?.let { sectionId ->
                        request.add(async { apis.getProducts(sectionId) })
                    }
                }

                val result = request.awaitAll()

                response.data?.forEachIndexed { index, section ->
                    section.products = result[index]
                }
            }

            LoadResult.Page(
                data = response.data?.map { section ->
                    MainSection(
                        sectionId = section.id,
                        type = SectionType.entries.find { it.label == section.type } ?: SectionType.NONE,
                        title = section.title,
                        products = section.products?.data ?: emptyList()
                    )
                } ?: emptyList(),
                prevKey = null,
                nextKey = if (response.paging?.nextPage != null) (params.key ?: 1) + 1 else null
            )
        }.getOrElse { e ->
            Log.printStackTrace(e)
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, MainSection>): Int = 1
}