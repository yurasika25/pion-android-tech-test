package com.example.sbtechnicaltest.feature.photos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sbtechnicaltest.feature.photos.model.PhotoItem
import com.example.sbtechnicaltest.feature.photos.usecase.FilterPhotosUseCase
import com.example.sbtechnicaltest.feature.photos.usecase.GetPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Coordinates paged photo loading, retry, and debounced local title filtering for the Photos screen.
 *
 * The first request starts at offset zero; subsequent requests skip the accumulated item count until
 * the API total is reached. Search filters all pages loaded so far and never triggers API calls.
 * Initial and next-page failures are kept separate so pagination errors do not replace existing rows.
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val filterPhotosUseCase: FilterPhotosUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhotosUiState())
    val uiState: StateFlow<PhotosUiState> = _uiState.asStateFlow()

    private val searchQueries = MutableStateFlow("")
    private var allPhotos: List<PhotoItem> = emptyList()
    private var appliedSearchQuery: String = ""
    private var loadJob: Job? = null

    init {
        observeSearchQueries()
        loadFirstPage()
    }

    fun onSearchQueryChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = value,
            )
        }
        searchQueries.value = value
    }

    fun onRetryClicked() {
        loadFirstPage()
    }

    fun onLoadMoreRequested() {
        val currentState = _uiState.value
        if (
            currentState.isLoading ||
            currentState.isLoadingMore ||
            !currentState.hasMorePages ||
            loadJob?.isActive == true
        ) {
            return
        }

        loadJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingMore = true,
                    loadMoreErrorMessage = null,
                )
            }

            getPhotosUseCase(
                limit = PAGE_SIZE,
                skip = allPhotos.size,
            )
                .onSuccess { page ->
                    allPhotos = allPhotos + page.photos
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            photos = filterPhotosUseCase(
                                photos = allPhotos,
                                query = appliedSearchQuery,
                            ),
                            loadMoreErrorMessage = null,
                            hasMorePages = page.photos.isNotEmpty() &&
                                page.skip + page.photos.size < page.total,
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            loadMoreErrorMessage = error.message,
                        )
                    }
                }
        }
    }

    private fun observeSearchQueries() {
        viewModelScope.launch {
            // Query text updates immediately; only local filtering waits for the debounce window.
            searchQueries
                .drop(1)
                .debounce(SEARCH_DEBOUNCE_MILLIS)
                .map(String::trim)
                .distinctUntilChanged()
                .collect { query ->
                    appliedSearchQuery = query
                    _uiState.update {
                        it.copy(
                            photos = filterPhotosUseCase(allPhotos, query),
                        )
                    }
                }
        }
    }

    private fun loadFirstPage() {
        loadJob?.cancel()
        allPhotos = emptyList()
        loadJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isLoadingMore = false,
                    photos = emptyList(),
                    errorMessage = null,
                    loadMoreErrorMessage = null,
                    hasMorePages = true,
                )
            }

            getPhotosUseCase(
                limit = PAGE_SIZE,
                skip = 0,
            )
                .onSuccess { page ->
                    allPhotos = page.photos
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            photos = filterPhotosUseCase(
                                photos = allPhotos,
                                query = appliedSearchQuery,
                            ),
                            hasMorePages = page.photos.isNotEmpty() &&
                                page.skip + page.photos.size < page.total,
                        )
                    }
                }
                .onFailure { error ->
                    allPhotos = emptyList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            photos = emptyList(),
                            errorMessage = error.message,
                            hasMorePages = false,
                        )
                    }
                }
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
        const val SEARCH_DEBOUNCE_MILLIS = 300L
    }
}
