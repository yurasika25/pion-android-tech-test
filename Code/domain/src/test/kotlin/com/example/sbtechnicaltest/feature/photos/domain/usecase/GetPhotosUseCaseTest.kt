package com.example.sbtechnicaltest.feature.photos.domain.usecase

import com.example.sbtechnicaltest.feature.photos.domain.model.PhotoItem
import com.example.sbtechnicaltest.feature.photos.domain.repository.PhotosRepository
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
        val repository = FakePhotosRepository(Result.success(photos))
        val useCase = GetPhotosUseCase(repository)

        val result = useCase()

        assertEquals(photos, result.getOrNull())
        assertEquals(1, repository.callCount)
    }

    @Test
    fun `returns repository failure`() = runTest {
        val expectedError = IllegalStateException("Network failed")
        val repository = FakePhotosRepository(Result.failure(expectedError))
        val useCase = GetPhotosUseCase(repository)

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }

    private class FakePhotosRepository(
        private val result: Result<List<PhotoItem>>,
    ) : PhotosRepository {

        var callCount: Int = 0
            private set

        override suspend fun getPhotos(): Result<List<PhotoItem>> {
            callCount += 1
            return result
        }
    }
}
