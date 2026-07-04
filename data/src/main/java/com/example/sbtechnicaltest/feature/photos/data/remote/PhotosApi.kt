package com.example.sbtechnicaltest.feature.photos.data.remote

import com.example.sbtechnicaltest.feature.photos.data.remote.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosApi {

    @GET("products")
    suspend fun getProducts(
        @Query("select") select: String = "id,title,thumbnail",
    ): ProductsResponseDto
}
