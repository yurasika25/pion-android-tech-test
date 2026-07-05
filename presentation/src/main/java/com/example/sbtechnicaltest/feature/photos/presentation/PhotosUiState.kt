package com.example.sbtechnicaltest.feature.photos.presentation

import androidx.annotation.StringRes
import com.example.sbtechnicaltest.feature.photos.domain.model.PhotoItem

data class PhotosUiState(
    val isLoading: Boolean = true,
    val photos: List<PhotoItem> = emptyList(),
    val searchQuery: String = "",
    @get:StringRes val errorMessageResId: Int? = null,
)
