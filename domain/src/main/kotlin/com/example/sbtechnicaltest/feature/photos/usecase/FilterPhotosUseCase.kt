package com.example.sbtechnicaltest.feature.photos.usecase

import com.example.sbtechnicaltest.feature.photos.model.PhotoItem

/** Applies trimmed, case-insensitive title filtering to an already loaded photo list. */
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
