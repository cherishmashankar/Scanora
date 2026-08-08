package com.cherishmashankar.scanora.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ScanoraDarkColorScheme = darkColorScheme(
    primary = ScanoraPrimary,
    onPrimary = ScanoraOnBackground,
    primaryContainer = ScanoraPrimaryContainer,
    onPrimaryContainer = ScanoraOnBackground,

    secondary = ScanoraSecondary,
    onSecondary = ScanoraBackground,
    secondaryContainer = ScanoraSecondaryContainer,
    onSecondaryContainer = ScanoraOnBackground,

    background = ScanoraBackground,
    onBackground = ScanoraOnBackground,

    surface = ScanoraSurface,
    onSurface = ScanoraOnSurface,

    surfaceVariant = ScanoraSurfaceVariant,
    onSurfaceVariant = ScanoraOnSurfaceVariant,

    error = ScanoraError,
    onError = ScanoraOnBackground,

    outline = ScanoraOutline
)

@Composable
fun ScanoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ScanoraDarkColorScheme,
        typography = Typography,
        content = content
    )
}