package com.rajith.pixabaypayback.data.datasource.remote.api

import com.rajith.pixabaypayback.domain.model.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {

    @GET("api/")
    suspend fun searchImages(
        @Query("q") searchString: String? = null,
        @Query("per_page") limit: Int? = null,
        @Query("page") page: Int? = null,
    ): ImageResponse
}