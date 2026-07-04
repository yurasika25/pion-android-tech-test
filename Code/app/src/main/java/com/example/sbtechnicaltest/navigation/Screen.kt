package com.example.sbtechnicaltest.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Photos : Screen("photos")
}
