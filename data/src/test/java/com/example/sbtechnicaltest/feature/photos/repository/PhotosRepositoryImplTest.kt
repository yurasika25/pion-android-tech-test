package com.example.sbtechnicaltest.feature.photos.repository

import com.example.sbtechnicaltest.feature.photos.remote.PhotosApi
import io.mockk.coEvery
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
    fun `timeout returns user friendly failure`() = runTest {
        coEvery { api.getProducts(any()) } throws SocketTimeoutException()

        val result = repository.getPhotos()

        assertEquals(
            "Request timed out. Please try again.",
            result.exceptionOrNull()?.message,
        )
    }

    @Test
    fun `unknown host returns no internet failure`() = runTest {
        coEvery { api.getProducts(any()) } throws UnknownHostException()

        val result = repository.getPhotos()

        assertEquals(
            "No internet connection. Please check your connection.",
            result.exceptionOrNull()?.message,
        )
    }

    @Test
    fun `unknown error returns photos fallback failure`() = runTest {
        coEvery { api.getProducts(any()) } throws IllegalStateException()

        val result = repository.getPhotos()

        assertEquals(
            "Unable to load photos. Please try again.",
            result.exceptionOrNull()?.message,
        )
    }
}
