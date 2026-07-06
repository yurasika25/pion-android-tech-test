package com.example.sbtechnicaltest.feature.photos.remote.dto

/** Remote page containing products and the metadata used to determine whether more are available. */
data class ProductsResponseDto(
    val products: List<ProductDto>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)
