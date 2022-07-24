package com.rajith.pixabaypayback.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rajith.pixabaypayback.data.common.PAGE_SIZE
import com.rajith.pixabaypayback.data.datasource.ImageRemoteMediator
import com.rajith.pixabaypayback.data.datasource.local.db.ImageDatabase
import com.rajith.pixabaypayback.data.datasource.remote.api.ImageApi
import com.rajith.pixabaypayback.domain.model.Image
import com.rajith.pixabaypayback.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class SearchRepositoryImpl @Inject constructor(
    private val api: ImageApi,
    private val database: ImageDatabase,
) : SearchRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun searchImages(query: String): Flow<PagingData<Image>> {
        val dbQuery = "%${query.replace(' ', '%')}%"
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { database.imageDao().queryImages(dbQuery)},
            remoteMediator = ImageRemoteMediator(
                api,
                query,
                database
            )
        ).flow
    }
}