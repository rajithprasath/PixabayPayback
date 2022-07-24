package com.rajith.pixabaypayback.domain.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.rajith.pixabaypayback.domain.model.Image
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    @ExperimentalPagingApi
    suspend fun searchImages(query: String): Flow<PagingData<Image>>
}