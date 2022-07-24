package com.rajith.pixabaypayback.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.rajith.pixabaypayback.data.common.FIRST_PAGE
import com.rajith.pixabaypayback.data.datasource.local.db.ImageDatabase
import com.rajith.pixabaypayback.data.datasource.remote.api.ImageApi
import com.rajith.pixabaypayback.domain.model.Image
import com.rajith.pixabaypayback.domain.model.RemoteKey
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ImageRemoteMediator(
    private val api: ImageApi,
    private val searchString: String,
    private val db: ImageDatabase
) : RemoteMediator<Int, Image>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Image>): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextPage?.minus(1) ?: FIRST_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevPage
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextPage
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val response = api.searchImages(searchString, state.config.pageSize, page)
            val images = response.images

            images.map {
                it.searchString = searchString
            }

            val endOfPaginationReached = images.isEmpty()
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.imageDao().clearAll()
                    db.remoteKeyDao().clearRemoteKeys()
                }
                val prevKey = if (page == FIRST_PAGE) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = images.map {
                    RemoteKey(prevPage = prevKey, nextPage = nextKey, imageId = it.id)
                }
                db.remoteKeyDao().insertAll(keys)
                db.imageDao().insertAll(images)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            println("djsjsjsjsjsjjjs 1111 " + exception.localizedMessage)
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            println("djsjsjsjsjsjjjs 222 " + exception.localizedMessage)
            return MediatorResult.Error(exception)
        }

    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Image>): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { image ->
                db.remoteKeyDao().remoteKeysImageId(image.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Image>): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { image ->
                db.remoteKeyDao().remoteKeysImageId(image.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Image>
    ): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { image ->
                db.remoteKeyDao().remoteKeysImageId(image.id)
            }
        }
    }
}