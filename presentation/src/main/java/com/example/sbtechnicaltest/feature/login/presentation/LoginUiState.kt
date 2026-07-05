package com.example.sbtechnicaltest.feature.login.presentation

import androidx.annotation.StringRes

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    @get:StringRes val usernameErrorResId: Int? = null,
    @get:StringRes val passwordErrorResId: Int? = null,
)
