package com.yourtime.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Pure & Deep Obsidian Palette
val OledBlack = Color(0xFF030508)
val OledSurface = Color(0xFF080C14)
val OledCard = Color(0xFF0D121D)
val OledCardBorder = Color(0xFF1B2437)
val OledCardBorderSubtle = Color(0xFF121927)

// Professional Liquid Accents (Refined Electric Cyan & Liquid Emerald)
val CyberCyan = Color(0xFF00E5FF)
val EmeraldGreen = Color(0xFF10B981)
val IceBlue = Color(0xFF38BDF8)
val CyanGlow = Color(0x1F00E5FF)
val EmeraldGlow = Color(0x1F10B981)

// Luxury Typography Colors
val TextWhite = Color(0xFFFFFFFF)
val TextMuted = Color(0xFF8896AB)
val TextSubtitle = Color(0xFFCBD5E1)
val TextHighlight = Color(0xFFF1F5F9)

// Functional
val ErrorRed = Color(0xFFFF5252)

// Fluid Liquid Gradient
val CyberGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF00E5FF), Color(0xFF10B981))
)

val LiquidFlowBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFF00E5FF),
        Color(0xFF06B6D4),
        Color(0xFF10B981)
    )
)

val CardGlowBorder = Brush.horizontalGradient(
    colors = listOf(Color(0x3300E5FF), Color(0x3310B981))
)
