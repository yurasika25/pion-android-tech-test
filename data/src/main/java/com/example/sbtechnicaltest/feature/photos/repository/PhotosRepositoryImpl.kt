package com.example.sbtechnicaltest.feature.photos.repository

import com.example.sbtechnicaltest.core.network.safeApiCall
import com.example.sbtechnicaltest.feature.photos.mapper.toDomain
import com.example.sbtechnicaltest.feature.photos.model.PhotosPage
import com.example.sbtechnicaltest.feature.photos.remote.PhotosApi
import javax.inject.Inject

/**
 * Loads the requested product page safely and maps its DTOs and metadata to domain models.
 */
class PhotosRepositoryImpl @Inject constructor(
    private val api: PhotosApi,
) : PhotosRepository {

    override suspend fun getPhotos(
        limit: Int,
        skip: Int,
    ): Result<PhotosPage> =
        safeApiCall(
            fallbackMessage = "Unable to load photos. Please try again.",
        ) {
            api.getProducts(
                limit = limit,
                skip = skip,
            ).toDomain()
        }
}
