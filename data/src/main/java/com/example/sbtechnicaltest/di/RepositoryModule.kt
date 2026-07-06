package com.example.sbtechnicaltest.di

import com.example.sbtechnicaltest.feature.photos.repository.PhotosRepositoryImpl
import com.example.sbtechnicaltest.feature.photos.repository.PhotosRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Binds the data implementation to the domain repository contract for constructor injection. */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPhotosRepository(
        implementation: PhotosRepositoryImpl,
    ): PhotosRepository
}
