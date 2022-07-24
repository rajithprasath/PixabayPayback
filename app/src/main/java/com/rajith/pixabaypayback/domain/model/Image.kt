package com.rajith.pixabaypayback.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "image_table")
@Parcelize
data class Image(
    @PrimaryKey(autoGenerate = true) val imageId: Int = 0,
    val comments: Int,
    val downloads: Int,
    val id: Int,
    @SerializedName("largeImageURL")
    val largeImageUrl: String,
    val likes: Int,
    val tags: String,
    val user: String,
    @SerializedName("user_id")
    val userId: Int,
    val views: Int,
    var searchString: String? = null
) : Parcelable