package com.rajith.pixabaypayback.data.datasource

import androidx.paging.PagingSource
import com.google.gson.Gson
import com.rajith.pixabaypayback.data.datasource.remote.api.ImageApi
import com.rajith.pixabaypayback.domain.model.Image
import com.rajith.pixabaypayback.domain.model.ImageResponse
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations.openMocks
import org.mockito.junit.MockitoJUnitRunner
import java.io.FileInputStream
import java.io.InputStream

@RunWith(MockitoJUnitRunner::class)
class ImageDataSourceTest {

    @Mock
    private lateinit var apiService: ImageApi
    private lateinit var pagingSource: ImageDataSource
    private lateinit var imageList: List<Image>
    private val gson = Gson()
    private lateinit var responseImageResponse: ImageResponse

    @Before
    fun setup() {
        openMocks(this)

        val jsonStream: InputStream = FileInputStream("src/main/assets/test.json")
        val jsonBytes: ByteArray = jsonStream.readBytes()

        responseImageResponse = gson.fromJson(String(jsonBytes), ImageResponse::class.java)
        imageList = responseImageResponse.images

        pagingSource = ImageDataSource("fruits", apiService)

    }

    @Test
    fun load_returns_page_when_success() = runBlocking {
        Mockito.`when`(apiService.searchImages(anyString(), anyInt(), anyInt()))
            .thenReturn(responseImageResponse)

        assertThat(
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            ),
            CoreMatchers.equalTo(
                PagingSource.LoadResult.Page(
                    data = imageList,
                    prevKey = null,
                    nextKey = 2
                )
            ),
        )
    }

}