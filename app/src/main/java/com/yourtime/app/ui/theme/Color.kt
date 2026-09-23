package com.yourtime.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Deep OLED Pitch Black Palette
val OledBlack = Color(0xFF000000)
val OledSurface = Color(0xFF080C14)
val OledCard = Color(0xFF0E1420)
val OledCardBorder = Color(0xFF1A2638)

// Neon Cyber-Cyan & Emerald Accents
val CyberCyan = Color(0xFF00F0FF)
val EmeraldGreen = Color(0xFF10B981)
val CyanGlow = Color(0x3300F0FF)
val EmeraldGlow = Color(0x3310B981)

// Text Colors
val TextWhite = Color(0xFFFFFFFF)
val TextMuted = Color(0xFF94A3B8)
val TextSubtitle = Color(0xFFCBD5E1)

// Functional
val ErrorRed = Color(0xFFFF5252)

// Cyber Neon Gradient
val CyberGradient = Brush.horizontalGradient(
    colors = listOf(CyberCyan, EmeraldGreen)
)

val CardGlowBorder = Brush.horizontalGradient(
    colors = listOf(CyberCyan.copy(alpha = 0.5f), EmeraldGreen.copy(alpha = 0.5f))
)
