package com.example.sbtechnicaltest.feature.photos.repository

import com.example.sbtechnicaltest.feature.photos.model.PhotoItem

interface PhotosRepository {
    suspend fun getPhotos(): Result<List<PhotoItem>>
}
