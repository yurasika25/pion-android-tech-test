package com.example.sbtechnicaltest.feature.photos.presentation

import com.example.sbtechnicaltest.MainDispatcherRule
import com.example.sbtechnicaltest.feature.photos.domain.model.PhotoItem
import com.example.sbtechnicaltest.feature.photos.domain.usecase.FilterPhotosUseCase
import com.example.sbtechnicaltest.feature.photos.domain.usecase.GetPhotosUseCase
import com.example.sbtechnicaltest.presentation.R
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotosViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getPhotosUseCase: GetPhotosUseCase
    private lateinit var filterPhotosUseCase: FilterPhotosUseCase

    private val photos = listOf(
        PhotoItem(1, "Golden sunrise", "https://example.com/1.jpg"),
        PhotoItem(2, "City skyline", "https://example.com/2.jpg"),
    )

    @Before
    fun setUp() {
        getPhotosUseCase = mockk()
        filterPhotosUseCase = mockk()
        every {
            filterPhotosUseCase.invoke(any(), any())
        } answers {
            val source = firstArg<List<PhotoItem>>()
            val query = secondArg<String>().trim()
            if (query.isEmpty()) {
                source
            } else {
                source.filter { it.title.contains(query, ignoreCase = true) }
            }
        }
    }

    @Test
    fun `successful load exposes photos`() = runTest {
        coEvery { getPhotosUseCase.invoke() } returns Result.success(photos)

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(photos, viewModel.uiState.value.photos)
        assertNull(viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun `failed load exposes retryable error`() = runTest {
        coEvery {
            getPhotosUseCase.invoke()
        } returns Result.failure(IllegalStateException("Network failed"))

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.photos.isEmpty())
        assertEquals(
            R.string.photos_load_error,
            viewModel.uiState.value.errorMessageResId,
        )
    }

    @Test
    fun `search query filters loaded photos`() = runTest {
        coEvery { getPhotosUseCase.invoke() } returns Result.success(photos)
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onSearchQueryChanged("city")

        assertEquals("city", viewModel.uiState.value.searchQuery)
        assertEquals(listOf(photos[1]), viewModel.uiState.value.photos)
    }

    @Test
    fun `retry loads photos after failure`() = runTest {
        coEvery {
            getPhotosUseCase.invoke()
        } returnsMany listOf(
            Result.failure(IllegalStateException("Network failed")),
            Result.success(photos),
        )
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onRetryClicked()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(photos, viewModel.uiState.value.photos)
        assertNull(viewModel.uiState.value.errorMessageResId)
    }

    private fun createViewModel(): PhotosViewModel = PhotosViewModel(
        getPhotosUseCase = getPhotosUseCase,
        filterPhotosUseCase = filterPhotosUseCase,
    )
}
