package com.example.sbtechnicaltest.feature.photos.mapper

import com.example.sbtechnicaltest.feature.photos.remote.dto.ProductDto
import com.example.sbtechnicaltest.feature.photos.model.PhotoItem

/** Maps a remote DTO to the domain model before data leaves the data layer. */
fun ProductDto.toDomain(): PhotoItem = PhotoItem(
    id = id,
    title = title,
    thumbnailUrl = thumbnail,
)
