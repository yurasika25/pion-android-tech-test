package com.example.sbtechnicaltest.di

import com.example.sbtechnicaltest.feature.photos.repository.PhotosRepository
import com.example.sbtechnicaltest.feature.photos.usecase.FilterPhotosUseCase
import com.example.sbtechnicaltest.feature.photos.usecase.GetPhotosUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetPhotosUseCase(
        repository: PhotosRepository,
    ): GetPhotosUseCase = GetPhotosUseCase(repository)

    @Provides
    fun provideFilterPhotosUseCase(): FilterPhotosUseCase = FilterPhotosUseCase()
}
