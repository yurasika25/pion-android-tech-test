package com.example.sbtechnicaltest.feature.photos.presentation

import com.example.sbtechnicaltest.feature.photos.model.PhotoItem

/**
 * Immutable state for initial and next-page loading, locally filtered photos, and retryable errors.
 */
data class PhotosUiState(
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val photos: List<PhotoItem> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null,
    val loadMoreErrorMessage: String? = null,
    val hasMorePages: Boolean = true,
)
