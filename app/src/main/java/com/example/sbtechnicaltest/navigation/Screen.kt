package com.example.sbtechnicaltest.navigation

/** Routes owned by the app module's Navigation Compose graph. */
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Photos : Screen("photos")
}
