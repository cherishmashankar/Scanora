package com.cherishmashankar.scanora.feature.scanner.presentation.result.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import kotlin.math.abs
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun FlippableScanResultHeader(
    type: ScanResultType,
    codePreview: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCode by rememberSaveable {
        mutableStateOf(false)
    }

    var hasPlayedIntro by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(hasPlayedIntro) {
        if (!hasPlayedIntro) {
            delay(400.milliseconds)
            showCode = true

            delay(1400.milliseconds)
            showCode = false

            hasPlayedIntro = true
        }
    }

    var dragDistance by androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableFloatStateOf(0f)
    }

    val rotation by animateFloatAsState(
        targetValue = if (showCode) 180f else 0f,
        animationSpec = tween(durationMillis = 450),
        label = "result_header_flip"
    )

    val density = LocalDensity.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 210.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        dragDistance = 0f
                    },
                    onHorizontalDrag = { change, amount ->
                        change.consume()
                        dragDistance += amount
                    },
                    onDragEnd = {
                        val threshold = with(density) {
                            48.dp.toPx()
                        }

                        if (abs(dragDistance) >= threshold) {
                            showCode = !showCode
                        }

                        dragDistance = 0f
                    },
                    onDragCancel = {
                        dragDistance = 0f
                    }
                )
            }
            .clickable {
                showCode = !showCode
            }
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * this.density
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 210.dp),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                ScanResultTypeHeader(
                    type = type,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Box(
                    modifier = Modifier.graphicsLayer {
                        rotationY = 180f
                    },
                    contentAlignment = Alignment.Center
                ) {
                    codePreview()
                }
            }
        }
    }
}

@Preview(
    name = "Flippable Header - Website",
    showBackground = true
)
@Composable
private fun FlippableScanResultHeaderPreview() {
    MaterialTheme {
        FlippableScanResultHeader(
            type = ScanResultType.URL,
            codePreview = {
                Text(
                    text = "Barcode / QR preview",
                    textAlign = TextAlign.Center
                )
            }
        )
    }
}