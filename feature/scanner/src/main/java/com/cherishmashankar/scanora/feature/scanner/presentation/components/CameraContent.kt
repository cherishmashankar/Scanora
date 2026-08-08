package com.cherishmashankar.scanora.feature.scanner.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.cherishmashankar.scanora.feature.scanner.presentation.ScannerStatus
import com.cherishmashankar.scanora.feature.scanner.presentation.toStringRes

@Composable
internal fun CameraContent(
    isScanning: Boolean,
    isFlashEnabled: Boolean,
    scannerStatus: ScannerStatus,
    onBarcodeDetected: (Scan) -> Unit,
    onToggleFlash: () -> Unit,
    onCodeDetected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CameraPreview(
            isScanning = isScanning,
            isFlashEnabled = isFlashEnabled,
            onCodeDetected = onCodeDetected,
            onBarcodeDetected = onBarcodeDetected,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            IconButton(
                onClick = onToggleFlash,
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
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

            // Visual guide only.
            Box(
                modifier = Modifier
                    .size(
                        width = 320.dp,
                        height = 220.dp
                    )
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(24.dp)
                    )
            )

            Text(
                text = stringResource(scannerStatus.toStringRes()),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}