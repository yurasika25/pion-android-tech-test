package com.example.sbtechnicaltest.di

import com.example.sbtechnicaltest.feature.photos.data.repository.PhotosRepositoryImpl
import com.example.sbtechnicaltest.feature.photos.domain.repository.PhotosRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPhotosRepository(
        implementation: PhotosRepositoryImpl,
    ): PhotosRepository
}
