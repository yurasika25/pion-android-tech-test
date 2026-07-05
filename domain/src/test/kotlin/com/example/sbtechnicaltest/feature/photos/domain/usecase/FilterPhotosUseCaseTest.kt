package com.example.sbtechnicaltest.feature.photos.domain.usecase

import com.example.sbtechnicaltest.feature.photos.model.PhotoItem
import com.example.sbtechnicaltest.feature.photos.usecase.FilterPhotosUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterPhotosUseCaseTest {

    private val useCase = FilterPhotosUseCase()
    private val photos = listOf(
        PhotoItem(1, "Golden sunrise", "https://example.com/1.jpg"),
        PhotoItem(2, "City skyline", "https://example.com/2.jpg"),
        PhotoItem(3, "Sunset beach", "https://example.com/3.jpg"),
    )

    @Test
    fun `blank query returns all photos`() {
        val result = useCase(photos, "  ")

        assertEquals(photos, result)
    }

    @Test
    fun `query filters titles ignoring case`() {
        val result = useCase(photos, "SUN")

        assertEquals(listOf(photos[0], photos[2]), result)
    }
}
