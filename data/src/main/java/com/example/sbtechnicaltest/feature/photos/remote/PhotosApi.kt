package com.example.sbtechnicaltest.feature.photos.remote

import com.example.sbtechnicaltest.feature.photos.remote.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosApi {

    @GET("products")
    suspend fun getProducts(
        @Query("select") select: String = "id,title,thumbnail",
    ): ProductsResponseDto
}
