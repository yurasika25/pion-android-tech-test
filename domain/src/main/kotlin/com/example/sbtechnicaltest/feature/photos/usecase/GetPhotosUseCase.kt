package com.example.sbtechnicaltest.feature.photos.usecase

import com.example.sbtechnicaltest.feature.photos.model.PhotosPage
import com.example.sbtechnicaltest.feature.photos.repository.PhotosRepository

/** Loads the requested photo page through the domain [PhotosRepository] abstraction. */
class GetPhotosUseCase(
    private val repository: PhotosRepository,
) {
    suspend operator fun invoke(
        limit: Int,
        skip: Int,
    ): Result<PhotosPage> = repository.getPhotos(
        limit = limit,
        skip = skip,
    )
}
