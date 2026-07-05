package com.example.sbtechnicaltest.feature.photos.mapper

import com.example.sbtechnicaltest.feature.photos.remote.dto.ProductDto
import com.example.sbtechnicaltest.feature.photos.model.PhotoItem

fun ProductDto.toDomain(): PhotoItem = PhotoItem(
    id = id,
    title = title,
    thumbnailUrl = thumbnail,
)
