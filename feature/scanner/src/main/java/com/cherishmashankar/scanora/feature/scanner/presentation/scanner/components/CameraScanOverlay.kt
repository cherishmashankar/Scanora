package com.cherishmashankar.scanora.feature.scanner.presentation.scanner.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
internal fun CameraScanOverlay(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
    ) {
        val frameWidth = 320.dp.toPx()
        val frameHeight = 220.dp.toPx()

        val left = (size.width - frameWidth) / 2f
        val top = (size.height - frameHeight) / 2f

        val scanRect = Rect(
            left = left,
            top = top,
            right = left + frameWidth,
            bottom = top + frameHeight
        )

        drawRect(
            color = Color.Black.copy(alpha = 0.55f)
        )

        drawRoundRect(
            color = Color.Transparent,
            topLeft = scanRect.topLeft,
            size = scanRect.size,
            cornerRadius = CornerRadius(
                x = 24.dp.toPx(),
                y = 24.dp.toPx()
            ),
            blendMode = BlendMode.Clear
        )

        drawRoundRect(
            color = Color.White,
            topLeft = scanRect.topLeft,
            size = scanRect.size,
            cornerRadius = CornerRadius(
                x = 24.dp.toPx(),
                y = 24.dp.toPx()
            ),
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = 2.dp.toPx()
            )
        )
    }
}