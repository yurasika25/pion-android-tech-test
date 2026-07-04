package com.example.sbtechnicaltest.feature.photos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sbtechnicaltest.feature.photos.domain.model.PhotoItem
import com.example.sbtechnicaltest.feature.photos.domain.usecase.FilterPhotosUseCase
import com.example.sbtechnicaltest.feature.photos.domain.usecase.GetPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val filterPhotosUseCase: FilterPhotosUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhotosUiState())
    val uiState: StateFlow<PhotosUiState> = _uiState.asStateFlow()

    private var allPhotos: List<PhotoItem> = emptyList()
    private var loadJob: Job? = null

    init {
        loadPhotos()
    }

    fun onSearchQueryChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = value,
                photos = filterPhotosUseCase(allPhotos, value),
            )
        }
    }

    fun onRetryClicked() {
        loadPhotos()
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
                                query = currentState.searchQuery,
                            ),
                        )
                    }
                }
                .onFailure {
                    allPhotos = emptyList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            photos = emptyList(),
                            errorMessage = LOAD_ERROR_MESSAGE,
                        )
                    }
                }
        }
    }

    private companion object {
        const val LOAD_ERROR_MESSAGE = "Unable to load photos. Please try again."
    }
}
