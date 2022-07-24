package com.rajith.pixabaypayback.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rajith.pixabaypayback.data.common.FIRST_PAGE
import com.rajith.pixabaypayback.data.datasource.remote.api.ImageApi
import com.rajith.pixabaypayback.domain.model.Image
import java.io.IOException

class ImageDataSource(
    private val query: String,
    private val api: ImageApi,
) : PagingSource<Int, Image>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val page = params.key ?: FIRST_PAGE
        return try {
            val data = api.searchImages(query, params.loadSize, page)

            LoadResult.Page(
                data = data.images,
                prevKey = if (page == FIRST_PAGE) null else page - 1,
                nextKey = if (data.images.isEmpty()) null else page + 1
            )
        } catch (t: Throwable) {
            var exception = t
            if (t is IOException) {
                exception = IOException("Please check your internet connection and try again")
            }

            LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition
    }
}