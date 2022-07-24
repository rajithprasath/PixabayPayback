package com.rajith.pixabaypayback.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.rajith.pixabaypayback.data.datasource.local.dao.ImageDao
import com.rajith.pixabaypayback.data.datasource.local.dao.RemoteKeyDao
import com.rajith.pixabaypayback.data.datasource.local.db.ImageDatabase
import com.rajith.pixabaypayback.domain.model.ImageResponse
import com.rajith.pixabaypayback.domain.model.RemoteKey
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.InputStream
import kotlin.random.Random


class ImageDatabaseTest {

    private lateinit var db: ImageDatabase
    private lateinit var context: Context
    private lateinit var remoteKeyDao: RemoteKeyDao
    private lateinit var imageDao: ImageDao
    private val gson = Gson()

    @Before
    fun setUp() {

        context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, ImageDatabase::class.java).build()
        val jsonStream: InputStream = context.resources.assets.open("test.json")
        val jsonBytes: ByteArray = jsonStream.readBytes()

        val images = gson.fromJson(String(jsonBytes), ImageResponse::class.java).images

        images.map {
            it.searchString = "fruits"
        }

        imageDao = db.imageDao()
        remoteKeyDao = db.remoteKeyDao()

        val keys = images.map {
            RemoteKey(0, it.id, 1, 2)
        }

        runBlocking {
            db.withTransaction {
                imageDao.insertAll(images)
                remoteKeyDao.insertAll(keys)
            }
        }
    }

    @Test
    fun return_true_number_keys_equal_images() = runBlocking {
        assertThat(
            imageDao.getAll().size, CoreMatchers.equalTo(
                remoteKeyDao.getAll().size
            )
        )
    }

    @Test
    fun return_true_if_search_term_is_fruits() = runBlocking {
        val result = imageDao.getAll()[Random(0).nextInt(10)].searchString == "fruits"
        assertThat(result, CoreMatchers.equalTo(true))
    }

    @After
    fun clear() {
        db.clearAllTables()
        db.close()
    }
}