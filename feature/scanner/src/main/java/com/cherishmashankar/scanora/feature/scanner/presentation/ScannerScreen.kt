package com.cherishmashankar.scanora.feature.scanner.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.feature.scanner.presentation.components.CameraContent
import com.cherishmashankar.scanora.feature.scanner.presentation.components.PermissionContent

@Composable
internal fun ScannerScreen(
    uiState: ScannerUiState,
    onBarcodeDetected: (Scan) -> Unit,
    onToggleFlash: () -> Unit,
    onCodeDetected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            !uiState.hasCameraPermission -> {
                PermissionContent()
            }

            else -> {
                CameraContent(
                    isScanning = uiState.isScanning,
                    isFlashEnabled = uiState.isFlashEnabled,
                    scannerStatus = uiState.scannerStatus,
                    onCodeDetected = onCodeDetected,
                    onBarcodeDetected = onBarcodeDetected,
                    onToggleFlash = onToggleFlash,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}