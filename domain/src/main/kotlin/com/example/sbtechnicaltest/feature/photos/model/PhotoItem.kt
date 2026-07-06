package com.example.sbtechnicaltest.feature.photos.model

/** UI-independent photo model shared through the domain contracts. */
data class PhotoItem(
    val id: Int,
    val title: String,
    val thumbnailUrl: String,
)
