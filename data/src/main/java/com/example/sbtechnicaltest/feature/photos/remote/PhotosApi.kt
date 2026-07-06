package com.example.sbtechnicaltest.feature.photos.remote

import com.example.sbtechnicaltest.feature.photos.remote.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit contract for requesting offset-based product pages from DummyJSON.
 *
 * `limit` controls page size, `skip` is the number of previously loaded products, and the
 * selected fields keep each response limited to data required by the Photos UI.
 */
interface PhotosApi {

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
        @Query("select") select: String = "id,title,thumbnail",
    ): ProductsResponseDto
}
