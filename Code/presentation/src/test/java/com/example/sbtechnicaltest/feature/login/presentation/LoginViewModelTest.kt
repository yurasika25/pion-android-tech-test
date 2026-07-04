package com.example.sbtechnicaltest.feature.login.presentation

import com.example.sbtechnicaltest.MainDispatcherRule
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
            "Username or email is required",
            viewModel.uiState.value.usernameError,
        )
        assertNull(viewModel.uiState.value.passwordError)
    }

    @Test
    fun `empty password shows password error`() {
        viewModel.onUsernameChanged("student@example.com")

        viewModel.onLoginClicked()

        assertNull(viewModel.uiState.value.usernameError)
        assertEquals(
            "Password is required",
            viewModel.uiState.value.passwordError,
        )
    }

    @Test
    fun `empty credentials show both errors`() {
        viewModel.onLoginClicked()

        assertEquals(
            "Username or email is required",
            viewModel.uiState.value.usernameError,
        )
        assertEquals(
            "Password is required",
            viewModel.uiState.value.passwordError,
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
