package com.example.sbtechnicaltest.feature.photos.mapper

import com.example.sbtechnicaltest.feature.photos.remote.dto.ProductDto
import com.example.sbtechnicaltest.feature.photos.remote.dto.ProductsResponseDto
import com.example.sbtechnicaltest.feature.photos.model.PhotoItem
import com.example.sbtechnicaltest.feature.photos.model.PhotosPage

/** Maps a remote DTO to the domain model before data leaves the data layer. */
fun ProductDto.toDomain(): PhotoItem = PhotoItem(
    id = id,
    title = title,
    thumbnailUrl = thumbnail,
)

/** Maps the remote page and its pagination metadata into domain models. */
fun ProductsResponseDto.toDomain(): PhotosPage = PhotosPage(
    photos = products.map(ProductDto::toDomain),
    total = total,
    skip = skip,
    limit = limit,
)
