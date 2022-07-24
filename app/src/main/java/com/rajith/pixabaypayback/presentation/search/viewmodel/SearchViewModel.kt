package com.rajith.pixabaypayback.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.rajith.pixabaypayback.data.common.DEFAULT_SEARCH
import com.rajith.pixabaypayback.domain.model.Image
import com.rajith.pixabaypayback.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val defaultSearch = DEFAULT_SEARCH
    var currentSearch = defaultSearch

    init {
        viewModelScope.launch {
            searchImages(currentSearch)
        }
    }

    suspend fun searchImages(searchString: String): Flow<PagingData<Image>> {
        currentSearch = searchString
        return searchUseCase.invoke(searchString)
    }

}