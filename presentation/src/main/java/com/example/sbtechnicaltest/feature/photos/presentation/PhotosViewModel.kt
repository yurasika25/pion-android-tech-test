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
 * Coordinates photo loading, retry, and debounced local title filtering for the Photos screen.
 *
 * It depends only on domain use cases and retains the loaded list so search never triggers API calls.
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
        loadPhotos()
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
        loadPhotos()
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

    private fun loadPhotos() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            }

            getPhotosUseCase()
                .onSuccess { photos ->
                    allPhotos = photos
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            photos = filterPhotosUseCase(
                                photos = photos,
                                query = appliedSearchQuery,
                            ),
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
                        )
                    }
                }
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MILLIS = 300L
    }
}
