package com.example.sbtechnicaltest.feature.photos.domain.usecase

import com.example.sbtechnicaltest.feature.photos.domain.model.PhotoItem
import com.example.sbtechnicaltest.feature.photos.domain.repository.PhotosRepository

class GetPhotosUseCase(
    private val repository: PhotosRepository,
) {
    suspend operator fun invoke(): Result<List<PhotoItem>> = repository.getPhotos()
}
