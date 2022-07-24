package com.rajith.pixabaypayback.data.datasource.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rajith.pixabaypayback.domain.model.Image

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<Image>)

    @Query("SELECT * FROM image_table WHERE searchString LIKE :query")
    fun queryImages(query: String): PagingSource<Int, Image>

    @Query("DELETE FROM image_table")
    suspend fun clearAll()

    @Query("SELECT * FROM image_table")
    suspend fun getAll(): List<Image>

}