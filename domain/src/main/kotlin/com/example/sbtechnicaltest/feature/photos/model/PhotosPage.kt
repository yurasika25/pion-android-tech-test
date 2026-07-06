package com.example.sbtechnicaltest.feature.photos.model

data class PhotosPage(
    val photos: List<PhotoItem>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)
