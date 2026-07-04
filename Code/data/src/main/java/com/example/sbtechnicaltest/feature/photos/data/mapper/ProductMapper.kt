package com.example.sbtechnicaltest.feature.photos.data.mapper

import com.example.sbtechnicaltest.feature.photos.data.remote.dto.ProductDto
import com.example.sbtechnicaltest.feature.photos.domain.model.PhotoItem

fun ProductDto.toDomain(): PhotoItem = PhotoItem(
    id = id,
    title = title,
    thumbnailUrl = thumbnail,
)
