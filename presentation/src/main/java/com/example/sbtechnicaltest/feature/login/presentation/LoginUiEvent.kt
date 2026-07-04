package com.example.sbtechnicaltest.feature.login.presentation

sealed interface LoginUiEvent {
    data object NavigateToPhotos : LoginUiEvent
}
