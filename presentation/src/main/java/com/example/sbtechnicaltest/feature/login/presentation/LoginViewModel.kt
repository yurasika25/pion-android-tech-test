package com.example.sbtechnicaltest.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = Channel<LoginUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onUsernameChanged(value: String) {
        _uiState.update {
            it.copy(
                username = value,
                usernameError = null,
            )
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordError = null,
            )
        }
    }

    fun onLoginClicked() {
        val currentState = _uiState.value
        val usernameError = if (currentState.username.isBlank()) {
            "Username or email is required"
        } else {
            null
        }
        val passwordError = if (currentState.password.isBlank()) {
            "Password is required"
        } else {
            null
        }

        _uiState.update {
            it.copy(
                usernameError = usernameError,
                passwordError = passwordError,
            )
        }

        if (usernameError == null && passwordError == null) {
            viewModelScope.launch {
                _events.send(LoginUiEvent.NavigateToPhotos)
            }
        }
    }
}
