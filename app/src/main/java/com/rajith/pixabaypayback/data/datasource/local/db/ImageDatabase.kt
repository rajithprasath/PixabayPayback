package com.rajith.pixabaypayback.data.datasource.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rajith.pixabaypayback.data.common.DB_NAME
import com.rajith.pixabaypayback.data.datasource.local.dao.ImageDao
import com.rajith.pixabaypayback.data.datasource.local.dao.RemoteKeyDao
import com.rajith.pixabaypayback.domain.model.Image
import com.rajith.pixabaypayback.domain.model.RemoteKey
import javax.inject.Singleton

@Database(
    entities = [Image::class, RemoteKey::class],
    version = 5, exportSchema = false
)
@Singleton
abstract class ImageDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        @Volatile
        private var instance: ImageDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance
                    ?: buildDatabase(
                        context
                    ).also {
                        instance = it
                    }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ImageDatabase::class.java,
                DB_NAME
            ).fallbackToDestructiveMigration()
                .build()
    }

}