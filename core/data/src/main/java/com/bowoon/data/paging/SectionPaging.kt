package com.bowoon.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bowoon.common.Log
import com.bowoon.model.MainSection
import com.bowoon.model.SectionType
import com.bowoon.network.KurlyDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SectionPaging @Inject constructor(
    private val apis: KurlyDataSource
) : PagingSource<Int, MainSection>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MainSection> =
        runCatching {
            val loadKey = params.key ?: 1
            val (section, nextKey) = coroutineScope {
                apis.getSections(page = loadKey).let { response ->
                    response.data?.mapNotNull { section ->
                        section.id?.let { sectionId ->
                            async(Dispatchers.IO) { apis.getProducts(sectionId) }
                        }
                    }?.awaitAll()?.mapIndexed { index, products ->
                        MainSection(
                            sectionId = response.data?.get(index)?.id,
                            type = SectionType.entries.find { it.label == response.data?.get(index)?.type } ?: SectionType.NONE,
                            title = response.data?.get(index)?.title,
                            products = products.data
                        )
                    }?.filter { !it.products.isNullOrEmpty() } to response.paging?.nextPage
                }
            }

            LoadResult.Page(
                data = section ?: emptyList(),
                prevKey = null,
                nextKey = if (nextKey != null) loadKey + 1 else null
            )
        }.getOrElse { e ->
            Log.printStackTrace(e)
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, MainSection>): Int = 1
}