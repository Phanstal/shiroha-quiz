package com.fufu.quiz.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = FufuColors.BrandPrimary,
    onPrimary = FufuColors.TextOnBrand,
    secondary = FufuColors.BrandSecondary,
    background = FufuColors.BgApp,
    onBackground = FufuColors.TextPrimary,
    surface = FufuColors.BgElevated,
    onSurface = FufuColors.TextPrimary,
    onSurfaceVariant = FufuColors.TextSecondary,
    outline = FufuColors.LineSoft
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF89A7FF),
    onPrimary = Color(0xFF0F172A),
    secondary = Color(0xFFB7C8FF),
    background = FufuColors.BgDeepFocus,
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF13203A),
    onSurface = Color(0xFFF8FAFC),
    onSurfaceVariant = Color(0xFFB8C2D6),
    outline = Color(0x33D7DEEA)
)

@Composable
fun FUFUQuizTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    FufuColors.isDarkMode = darkTheme

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = FufuTypography,
        content = content
    )
}
