package com.example.sbtechnicaltest.feature.login.presentation

import com.example.sbtechnicaltest.MainDispatcherRule
import com.example.sbtechnicaltest.presentation.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = LoginViewModel()
    }

    @Test
    fun `empty username shows username error`() {
        viewModel.onPasswordChanged("password")

        viewModel.onLoginClicked()

        assertEquals(
            R.string.login_username_or_email_required_error,
            viewModel.uiState.value.usernameErrorResId,
        )
        assertNull(viewModel.uiState.value.passwordErrorResId)
    }

    @Test
    fun `empty password shows password error`() {
        viewModel.onUsernameChanged("student@example.com")

        viewModel.onLoginClicked()

        assertNull(viewModel.uiState.value.usernameErrorResId)
        assertEquals(
            R.string.login_password_required_error,
            viewModel.uiState.value.passwordErrorResId,
        )
    }

    @Test
    fun `empty credentials show both errors`() {
        viewModel.onLoginClicked()

        assertEquals(
            R.string.login_username_or_email_required_error,
            viewModel.uiState.value.usernameErrorResId,
        )
        assertEquals(
            R.string.login_password_required_error,
            viewModel.uiState.value.passwordErrorResId,
        )
    }

    @Test
    fun `valid credentials emit navigation event`() = runTest {
        viewModel.onUsernameChanged("student@example.com")
        viewModel.onPasswordChanged("password")
        val event = async { viewModel.events.first() }

        viewModel.onLoginClicked()

        assertEquals(LoginUiEvent.NavigateToPhotos, event.await())
    }
}
