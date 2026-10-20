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
    // Primary actions
    primary = Color(0xFFF5F5F5),
    onPrimary = Color(0xFF0A0A0A),
    primaryContainer = Color(0xFF292929),
    onPrimaryContainer = Color(0xFFFFFFFF),

    // Secondary accents
    secondary = Color(0xFFD0D0D0),
    onSecondary = Color(0xFF111111),
    secondaryContainer = Color(0xFF252525),
    onSecondaryContainer = Color(0xFFECECEC),

    // Background
    background = Color(0xFF000000),
    onBackground = Color(0xFFF5F5F5),

    // Base surfaces
    surface = Color(0xFF000000),
    onSurface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFF1C1C1C),
    onSurfaceVariant = Color(0xFFB5B5B5),

    // Surface elevation hierarchy
    surfaceDim = Color(0xFF000000),
    surfaceBright = Color(0xFF303030),
    surfaceContainerLowest = Color(0xFF080808),
    surfaceContainerLow = Color(0xFF111111),
    surfaceContainer = Color(0xFF191919),
    surfaceContainerHigh = Color(0xFF222222),
    surfaceContainerHighest = Color(0xFF2B2B2B),

    // Borders and dividers
    outline = Color(0xFF666666),
    outlineVariant = Color(0xFF303030),

    // Errors
    error = Color(0xFFFF7777),
    onError = Color(0xFF330000),
    errorContainer = Color(0xFF6B2020),
    onErrorContainer = Color(0xFFFFDADA),

    // Inverse colors
    inverseSurface = Color(0xFFF2F2F2),
    inverseOnSurface = Color(0xFF161616),
    inversePrimary = Color(0xFF333333),

    // Scrim
    scrim = Color.Black
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