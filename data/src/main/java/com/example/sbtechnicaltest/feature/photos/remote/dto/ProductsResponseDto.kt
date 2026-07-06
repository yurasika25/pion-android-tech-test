package com.example.sbtechnicaltest.feature.photos.remote.dto

/** Response envelope returned by the products endpoint. */
data class ProductsResponseDto(
    val products: List<ProductDto>,
)
