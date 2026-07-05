package com.example.sbtechnicaltest.presentation.design

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF24B8C7),
    background = Color(0xFFFAFBFB),
    surface = Color(0xFFFAFBFB),
    surfaceVariant = Color(0xFFF0F4F5),
    onBackground = Color(0xFF262626),
    onSurface = Color(0xFF262626),
)
private val DarkColorScheme = darkColorScheme()

@Composable
fun SBTechnicalTestTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content,
    )
}
