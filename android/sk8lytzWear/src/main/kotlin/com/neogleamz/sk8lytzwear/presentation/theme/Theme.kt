package com.neogleamz.sk8lytzwear.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Colors

/**
 * SK8Lytz AMOLED Dark Theme for Wear OS.
 *
 * Design philosophy: true black (#000) saves battery on AMOLED displays.
 * Electric cyan (#00F0FF) is the SK8Lytz brand accent.
 * Neon magenta used for health/HR metrics.
 */

// Brand colors — shared across the Wear OS app
val TrueBlack = Color(0xFF000000)
val ElectricCyan = Color(0xFF00F0FF)
val NeonMagenta = Color(0xFFFF2D7B)
val BrandOrange = Color(0xFFF79320)
val SurfaceDark = Color(0xFF1A1A1A)

private val SK8LytzWearColors = Colors(
    primary = ElectricCyan,
    primaryVariant = Color(0xFF00BCD4),
    secondary = NeonMagenta,
    secondaryVariant = Color(0xFFFF5599),
    background = TrueBlack,
    surface = SurfaceDark,
    error = Color(0xFFFF3B3B),
    onPrimary = TrueBlack,
    onSecondary = TrueBlack,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = TrueBlack
)

@Composable
fun SK8LytzWearTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = SK8LytzWearColors,
        content = content
    )
}
