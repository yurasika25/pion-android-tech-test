package com.example.sbtechnicaltest.feature.photos.repository

import com.example.sbtechnicaltest.feature.photos.model.PhotosPage

interface PhotosRepository {
    suspend fun getPhotos(
        limit: Int,
        skip: Int,
    ): Result<PhotosPage>
}
