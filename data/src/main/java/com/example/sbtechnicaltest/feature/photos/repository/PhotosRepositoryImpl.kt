package com.example.sbtechnicaltest.feature.photos.repository

import com.example.sbtechnicaltest.feature.photos.remote.PhotosApi
import com.example.sbtechnicaltest.feature.photos.mapper.toDomain
import com.example.sbtechnicaltest.feature.photos.model.PhotoItem
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
