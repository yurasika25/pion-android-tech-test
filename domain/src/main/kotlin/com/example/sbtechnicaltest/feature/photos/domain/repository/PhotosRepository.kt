package com.example.sbtechnicaltest.feature.photos.domain.repository

import com.example.sbtechnicaltest.feature.photos.domain.model.PhotoItem

interface PhotosRepository {
    suspend fun getPhotos(): Result<List<PhotoItem>>
}
