package com.yourtime.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Ultra-premium Sunset Twilight / Cosmic Dark Theme
val BackgroundDark = Color(0xFF0C0F17)
val SurfaceDark = Color(0xFF131823)
val CardBackground = Color(0xFF19202E)
val CardBorder = Color(0xFF263246)

// Sunset Coral & Amber Gradient Palette
val CoralPrimary = Color(0xFFFF6347)
val AmberSecondary = Color(0xFFFF8A3D)
val CoralLight = Color(0xFFFF8566)
val CoralGlow = Color(0x33FF6347)

// Text Colors
val TextWhite = Color(0xFFFFFFFF)
val TextMuted = Color(0xFF8E9BAE)
val TextSubtitle = Color(0xFFBAC5D5)

// Functional & Accents
val LiveGreen = Color(0xFF10B981)
val ErrorRed = Color(0xFFEF4444)

// Pre-defined Brushes
val SunsetGradient = Brush.horizontalGradient(
    colors = listOf(CoralPrimary, AmberSecondary)
)

val CardBackgroundBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFF1C2433), Color(0xFF161C28))
)

val MountainHorizonBrush = Brush.verticalGradient(
    colors = listOf(Color.Transparent, Color(0x33FF6347), Color(0x661A1320), BackgroundDark)
)
