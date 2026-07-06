package com.example.sbtechnicaltest.feature.photos.repository

import com.example.sbtechnicaltest.core.network.safeApiCall
import com.example.sbtechnicaltest.feature.photos.remote.PhotosApi
import com.example.sbtechnicaltest.feature.photos.mapper.toDomain
import com.example.sbtechnicaltest.feature.photos.model.PhotoItem
import javax.inject.Inject

/**
 * Implements the domain repository by executing the API safely and mapping DTOs to domain models.
 */
class PhotosRepositoryImpl @Inject constructor(
    private val api: PhotosApi,
) : PhotosRepository {

    override suspend fun getPhotos(): Result<List<PhotoItem>> =
        safeApiCall(
            fallbackMessage = "Unable to load photos. Please try again.",
        ) {
            api.getProducts().products.map { product ->
                product.toDomain()
            }
        }
}
