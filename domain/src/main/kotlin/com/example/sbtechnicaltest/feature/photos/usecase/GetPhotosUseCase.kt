package com.example.sbtechnicaltest.feature.photos.usecase

import com.example.sbtechnicaltest.feature.photos.model.PhotoItem
import com.example.sbtechnicaltest.feature.photos.repository.PhotosRepository

/** Presentation-to-domain boundary for loading photos through [PhotosRepository]. */
class GetPhotosUseCase(
    private val repository: PhotosRepository,
) {
    suspend operator fun invoke(): Result<List<PhotoItem>> = repository.getPhotos()
}
