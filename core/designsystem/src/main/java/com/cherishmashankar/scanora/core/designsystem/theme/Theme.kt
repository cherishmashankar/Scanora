package com.cherishmashankar.scanora.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//private val ScanoraDarkColorScheme = darkColorScheme(
//    primary = ScanoraPrimary,
//    onPrimary = ScanoraOnBackground,
//    primaryContainer = ScanoraPrimaryContainer,
//    onPrimaryContainer = ScanoraOnBackground,
//
//    secondary = ScanoraSecondary,
//    onSecondary = ScanoraBackground,
//    secondaryContainer = ScanoraSecondaryContainer,
//    onSecondaryContainer = ScanoraOnBackground,
//
//    background = ScanoraBackground,
//    onBackground = ScanoraOnBackground,
//
//    surface = ScanoraSurface,
//    onSurface = ScanoraOnSurface,
//
//    surfaceVariant = ScanoraSurfaceVariant,
//    onSurfaceVariant = ScanoraOnSurfaceVariant,
//
//    error = ScanoraError,
//    onError = ScanoraOnBackground,
//
//    outline = ScanoraOutline
//)

private val ScanoraDarkColors = darkColorScheme(
    primary = Color(0xFFF5F5F5),
    onPrimary = Color(0xFF111111),
    primaryContainer = Color(0xFF2A2A2A),
    onPrimaryContainer = Color(0xFFF5F5F5),

    background = Color(0xFF0D0D0D),
    onBackground = Color(0xFFF2F2F2),

    surface = Color(0xFF0D0D0D),
    onSurface = Color(0xFFF2F2F2),

    surfaceVariant = Color(0xFF1B1B1B),
    onSurfaceVariant = Color(0xFFB8B8B8),

    outline = Color(0xFF5A5A5A),
    outlineVariant = Color(0xFF303030),

    error = Color(0xFFFF6B6B),
    onError = Color.Black
)


@Composable
fun ScanoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ScanoraDarkColors,
        typography = Typography,
        content = content
    )
}