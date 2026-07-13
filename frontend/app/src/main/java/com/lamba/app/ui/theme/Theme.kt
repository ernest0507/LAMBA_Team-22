package com.lamba.app.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LambaLightColorScheme = lightColorScheme(
    primary = LambaAccent,
    onPrimary = Color.White,
    primaryContainer = LambaAccentSoft,
    onPrimaryContainer = LambaAccentStrong,
    secondary = LambaAccentStrong,
    onSecondary = Color.White,
    secondaryContainer = LambaSurfaceSoft,
    onSecondaryContainer = LambaInk,
    tertiary = LambaSignal,
    onTertiary = Color.White,
    background = LambaCanvas,
    onBackground = LambaInk,
    surface = LambaSurface,
    onSurface = LambaInk,
    surfaceVariant = LambaSurfaceSoft,
    onSurfaceVariant = LambaInkMuted,
    outline = LambaOutline,
    outlineVariant = LambaOutlineSoft,
    error = LambaError,
    onError = Color.White
)

private val LambaDarkColorScheme = darkColorScheme(
    primary = Color(0xFF35BCD0),
    onPrimary = Color(0xFF062A30),
    primaryContainer = Color(0xFF173A40),
    onPrimaryContainer = Color(0xFF9CE8F0),
    secondary = Color(0xFF67D1DE),
    onSecondary = Color(0xFF062A30),
    background = Color(0xFF0F1719),
    onBackground = Color(0xFFEAF2F1),
    surface = Color(0xFF172124),
    onSurface = Color(0xFFEAF2F1),
    surfaceVariant = Color(0xFF1D2B2E),
    onSurfaceVariant = Color(0xFFA8B8BA),
    outline = Color(0xFF344548),
    outlineVariant = Color(0xFF263639),
    error = Color(0xFFFF7A72),
    onError = Color(0xFF3A0603),
    errorContainer = Color(0xFF531815),
    onErrorContainer = Color(0xFFFFDAD6)
)

@Composable
fun LAMBA_MVPv0Theme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        LambaDarkColorScheme
    } else {
        LambaLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LambaTypography,
        content = content
    )
}
