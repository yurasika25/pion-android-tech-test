package com.example.sbtechnicaltest.feature.photos.presentation

import com.example.sbtechnicaltest.feature.photos.model.PhotoItem

/** Immutable state for loading, filtered photos, search input, and retryable errors. */
data class PhotosUiState(
    val isLoading: Boolean = true,
    val photos: List<PhotoItem> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null,
)
