package com.example.sbtechnicaltest.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sbtechnicaltest.presentation.R
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
                usernameErrorResId = null,
            )
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordErrorResId = null,
            )
        }
    }

    fun onLoginClicked() {
        val currentState = _uiState.value
        val usernameErrorResId = if (currentState.username.isBlank()) {
            R.string.login_username_or_email_required_error
        } else {
            null
        }
        val passwordErrorResId = if (currentState.password.isBlank()) {
            R.string.login_password_required_error
        } else {
            null
        }

        _uiState.update {
            it.copy(
                usernameErrorResId = usernameErrorResId,
                passwordErrorResId = passwordErrorResId,
            )
        }

        if (usernameErrorResId == null && passwordErrorResId == null) {
            viewModelScope.launch {
                _events.send(LoginUiEvent.NavigateToPhotos)
            }
        }
    }
}
