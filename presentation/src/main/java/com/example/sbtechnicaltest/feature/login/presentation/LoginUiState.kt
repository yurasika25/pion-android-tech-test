package com.example.sbtechnicaltest.feature.login.presentation

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
)
