package com.cherishmashankar.scanora.feature.scanner.presentation.scanner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.feature.scanner.R
import com.cherishmashankar.scanora.feature.scanner.camera.CameraPreview
import com.cherishmashankar.scanora.feature.scanner.presentation.scanner.ScannerStatus
import com.cherishmashankar.scanora.feature.scanner.presentation.scanner.toStringRes

@Composable
internal fun CameraContent(
    isScanning: Boolean,
    isFlashEnabled: Boolean,
    scannerStatus: ScannerStatus,
    onBarcodeDetected: (Scan) -> Unit,
    onToggleFlash: () -> Unit,
    onCodeDetected: () -> Unit,
    onCameraError: () -> Unit,
    cameraRetryKey: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CameraPreview(
            isScanning = isScanning,
            isFlashEnabled = isFlashEnabled,
            cameraRetryKey = cameraRetryKey,
            onCodeDetected = onCodeDetected,
            onBarcodeDetected = onBarcodeDetected,
            onCameraError = onCameraError,
            modifier = Modifier.fillMaxSize()
        )

        CameraScanOverlay(
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = onToggleFlash,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 100.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f),
                    shape = CircleShape
                )

        ) {
            Icon(
                imageVector = if (isFlashEnabled) {
                    Icons.Default.FlashOff
                } else {
                    Icons.Default.FlashOn
                },
                contentDescription = stringResource(R.string.toggle_flash),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = stringResource(scannerStatus.toStringRes()),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 140.dp)
        )
    }
}