package com.example.sbtechnicaltest.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sbtechnicaltest.feature.login.presentation.LoginScreen
import com.example.sbtechnicaltest.feature.login.presentation.LoginUiEvent
import com.example.sbtechnicaltest.feature.login.presentation.LoginViewModel
import com.example.sbtechnicaltest.feature.photos.presentation.PhotosRoute

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
    ) {
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel()
            val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(loginViewModel) {
                loginViewModel.events.collect { event ->
                    when (event) {
                        LoginUiEvent.NavigateToPhotos -> {
                            navController.navigate(Screen.Photos.route) {
                                popUpTo(Screen.Login.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            }

            LoginScreen(
                uiState = uiState,
                onUsernameChanged = loginViewModel::onUsernameChanged,
                onPasswordChanged = loginViewModel::onPasswordChanged,
                onLoginClicked = loginViewModel::onLoginClicked,
            )
        }

        composable(Screen.Photos.route) {
            PhotosRoute()
        }
    }
}
