package com.rajith.pixabaypayback.data.di

import android.content.Context
import com.rajith.pixabaypayback.BuildConfig
import com.rajith.pixabaypayback.data.common.BASE_URL
import com.rajith.pixabaypayback.data.common.IMAGE_TYPE
import com.rajith.pixabaypayback.data.common.KEY
import com.rajith.pixabaypayback.data.datasource.local.db.ImageDatabase
import com.rajith.pixabaypayback.data.datasource.remote.api.ImageApi
import com.rajith.pixabaypayback.data.repository.SearchRepositoryImpl
import com.rajith.pixabaypayback.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    private val loggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(apiInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) okHttpClient.addInterceptor(loggingInterceptor)
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): ImageApi = retrofit.create(ImageApi::class.java)

    @Provides
    @Singleton
    fun providesRepository(
        api: ImageApi,
        database: ImageDatabase
    ): SearchRepository = SearchRepositoryImpl(api, database)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ImageDatabase {
        return ImageDatabase.invoke(appContext)
    }

    private val apiInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
        val originalHttpUrl = chain.request().url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(KEY.first, KEY.second)
            .addQueryParameter(IMAGE_TYPE.first, IMAGE_TYPE.second)
            .build()
        request.url(url)
        chain.proceed(request.build())
    }

}