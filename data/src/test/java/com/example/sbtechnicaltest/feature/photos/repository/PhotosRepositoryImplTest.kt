package com.example.sbtechnicaltest.feature.photos.repository

import com.example.sbtechnicaltest.feature.photos.remote.PhotosApi
import com.example.sbtechnicaltest.feature.photos.remote.dto.ProductDto
import com.example.sbtechnicaltest.feature.photos.remote.dto.ProductsResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class PhotosRepositoryImplTest {

    private val api: PhotosApi = mockk()
    private val repository = PhotosRepositoryImpl(api)

    @Test
    fun `maps requested page and pagination metadata`() = runTest {
        coEvery {
            api.getProducts(
                limit = 20,
                skip = 40,
                select = any(),
            )
        } returns ProductsResponseDto(
            products = listOf(
                ProductDto(
                    id = 1,
                    title = "Test photo",
                    thumbnail = "https://example.com/photo.jpg",
                ),
            ),
            total = 100,
            skip = 40,
            limit = 20,
        )

        val page = repository.getPhotos(
            limit = 20,
            skip = 40,
        ).getOrThrow()

        assertEquals(1, page.photos.single().id)
        assertEquals(100, page.total)
        assertEquals(40, page.skip)
        assertEquals(20, page.limit)
        coVerify(exactly = 1) {
            api.getProducts(
                limit = 20,
                skip = 40,
                select = "id,title,thumbnail",
            )
        }
    }

    @Test
    fun `timeout returns user friendly failure`() = runTest {
        coEvery { api.getProducts(any(), any(), any()) } throws SocketTimeoutException()

        val result = repository.getPhotos(limit = 20, skip = 0)

        assertEquals(
            "Request timed out. Please try again.",
            result.exceptionOrNull()?.message,
        )
    }

    @Test
    fun `unknown host returns no internet failure`() = runTest {
        coEvery { api.getProducts(any(), any(), any()) } throws UnknownHostException()

        val result = repository.getPhotos(limit = 20, skip = 0)

        assertEquals(
            "No internet connection. Please check your connection.",
            result.exceptionOrNull()?.message,
        )
    }

    @Test
    fun `unknown error returns photos fallback failure`() = runTest {
        coEvery { api.getProducts(any(), any(), any()) } throws IllegalStateException()

        val result = repository.getPhotos(limit = 20, skip = 0)

        assertEquals(
            "Unable to load photos. Please try again.",
            result.exceptionOrNull()?.message,
        )
    }
}
