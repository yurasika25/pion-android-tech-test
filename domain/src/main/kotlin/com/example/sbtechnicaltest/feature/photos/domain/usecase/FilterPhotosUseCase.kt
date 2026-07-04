package com.example.sbtechnicaltest.feature.photos.domain.usecase

import com.example.sbtechnicaltest.feature.photos.domain.model.PhotoItem

class FilterPhotosUseCase {

    operator fun invoke(
        photos: List<PhotoItem>,
        query: String,
    ): List<PhotoItem> {
        val normalizedQuery = query.trim()
        return if (normalizedQuery.isEmpty()) {
            photos
        } else {
            photos.filter { photo ->
                photo.title.contains(normalizedQuery, ignoreCase = true)
            }
        }
    }
}
