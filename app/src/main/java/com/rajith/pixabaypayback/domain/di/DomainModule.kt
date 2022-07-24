package com.rajith.pixabaypayback.domain.di

import com.rajith.pixabaypayback.domain.repository.SearchRepository
import com.rajith.pixabaypayback.domain.usecase.SearchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideSearchUseCase(searchRepository: SearchRepository) =
        SearchUseCase(searchRepository)
}