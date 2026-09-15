package com.yourtime.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CoralPrimary,
    onPrimary = TextWhite,
    primaryContainer = CardBackground,
    secondary = AmberSecondary,
    onSecondary = TextWhite,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = CardBackground,
    onBackground = TextWhite,
    onSurface = TextWhite,
    onSurfaceVariant = TextMuted,
    outline = CardBorder,
    error = ErrorRed
)

@Composable
fun YourTimeTheme(
    darkTheme: Boolean = true, // Designed as immersive deep nocturnal aesthetic
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
