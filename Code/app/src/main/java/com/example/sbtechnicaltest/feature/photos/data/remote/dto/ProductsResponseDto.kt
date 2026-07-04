package com.example.sbtechnicaltest.feature.photos.data.remote.dto

import com.example.sbtechnicaltest.feature.photos.domain.model.PhotoItem

data class ProductsResponseDto(
    val products: List<ProductDto>,
)

data class ProductDto(
    val id: Int,
    val title: String,
    val thumbnail: String,
)

fun ProductDto.toDomain(): PhotoItem = PhotoItem(
    id = id,
    title = title,
    thumbnailUrl = thumbnail,
)
