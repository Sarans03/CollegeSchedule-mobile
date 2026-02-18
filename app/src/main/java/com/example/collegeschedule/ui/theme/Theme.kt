package com.example.collegeschedule.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BrownPrimary,
    secondary = BrownLight,
    tertiary = BrownDark,
    background = CreamBackground,
    surface = CreamBackground,
    onPrimary = White,
    onSecondary = BrownPrimary,
    onTertiary = White,
    onBackground = BrownPrimary,
    onSurface = BrownPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = BrownLight,
    secondary = BrownLight,
    tertiary = BrownLight,
    background = Color(0xFF1F1F1F),
    surface = Color(0xFF2D2D2D),
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = White,
    onSurface = White
)

@Composable
fun CollegeScheduleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}