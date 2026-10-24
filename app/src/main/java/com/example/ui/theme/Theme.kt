package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightKaveriAccent = AccentKaveri

private val LightColorScheme = lightColorScheme(
    primary = LightKaveriAccent,
    background = LightBg,
    surface = LightSurface,
    surfaceVariant = LightSurface2,
    onBackground = LightInk,
    onSurface = LightInk,
    onSurfaceVariant = LightMuted,
    outline = LightLine
)

private val DarkColorScheme = darkColorScheme(
    primary = LightKaveriAccent,
    background = DarkBg,
    surface = DarkSurface,
    surfaceVariant = DarkSurface2,
    onBackground = DarkInk,
    onSurface = DarkInk,
    onSurfaceVariant = DarkMuted,
    outline = DarkLine
)

@Composable
fun SangamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    accentColor: Color = AccentKaveri,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme.copy(
            primary = accentColor,
            primaryContainer = accentColor.copy(alpha = 0.18f),
            onPrimaryContainer = accentColor
        )
    } else {
        LightColorScheme.copy(
            primary = accentColor,
            primaryContainer = accentColor.copy(alpha = 0.12f),
            onPrimaryContainer = accentColor
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SangamTypography,
        content = content
    )
}
