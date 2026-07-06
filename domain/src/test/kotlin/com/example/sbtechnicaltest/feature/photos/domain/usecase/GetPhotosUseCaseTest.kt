package com.example.sbtechnicaltest.feature.photos.domain.usecase

import com.example.sbtechnicaltest.feature.photos.model.PhotoItem
import com.example.sbtechnicaltest.feature.photos.model.PhotosPage
import com.example.sbtechnicaltest.feature.photos.repository.PhotosRepository
import com.example.sbtechnicaltest.feature.photos.usecase.GetPhotosUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetPhotosUseCaseTest {

    @Test
    fun `returns photos from repository`() = runTest {
        val photos = listOf(
            PhotoItem(
                id = 1,
                title = "Test photo",
                thumbnailUrl = "https://example.com/photo.jpg",
            ),
        )
        val page = PhotosPage(
            photos = photos,
            total = 42,
            skip = 20,
            limit = 20,
        )
        val repository = FakePhotosRepository(Result.success(page))
        val useCase = GetPhotosUseCase(repository)

        val result = useCase(
            limit = 20,
            skip = 20,
        )

        assertEquals(page, result.getOrNull())
        assertEquals(1, repository.callCount)
        assertEquals(20, repository.requestedLimit)
        assertEquals(20, repository.requestedSkip)
    }

    @Test
    fun `returns repository failure`() = runTest {
        val expectedError = IllegalStateException("Network failed")
        val repository = FakePhotosRepository(Result.failure(expectedError))
        val useCase = GetPhotosUseCase(repository)

        val result = useCase(
            limit = 20,
            skip = 0,
        )

        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }

    private class FakePhotosRepository(
        private val result: Result<PhotosPage>,
    ) : PhotosRepository {

        var callCount: Int = 0
            private set
        var requestedLimit: Int? = null
            private set
        var requestedSkip: Int? = null
            private set

        override suspend fun getPhotos(
            limit: Int,
            skip: Int,
        ): Result<PhotosPage> {
            callCount += 1
            requestedLimit = limit
            requestedSkip = skip
            return result
        }
    }
}
