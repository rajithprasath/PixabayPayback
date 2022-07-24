package com.rajith.pixabaypayback.domain.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.rajith.pixabaypayback.domain.model.Image
import com.rajith.pixabaypayback.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    @OptIn(ExperimentalPagingApi::class)
    fun invoke(query: String): Flow<PagingData<Image>> {
        return searchRepository.searchImages(query)
    }

}