package com.lamba.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LambaColorScheme = lightColorScheme(
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

@Composable
fun LAMBA_MVPv0Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LambaColorScheme,
        typography = LambaTypography,
        content = content
    )
}
