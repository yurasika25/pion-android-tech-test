package com.example.sbtechnicaltest.feature.photos.data.repository

import com.example.sbtechnicaltest.feature.photos.data.remote.PhotosApi
import com.example.sbtechnicaltest.feature.photos.data.remote.dto.toDomain
import com.example.sbtechnicaltest.feature.photos.domain.model.PhotoItem
import com.example.sbtechnicaltest.feature.photos.domain.repository.PhotosRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
    private val api: PhotosApi,
) : PhotosRepository {

    override suspend fun getPhotos(): Result<List<PhotoItem>> {
        return try {
            val photos = api.getProducts().products.map { product ->
                product.toDomain()
            }
            Result.success(photos)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
