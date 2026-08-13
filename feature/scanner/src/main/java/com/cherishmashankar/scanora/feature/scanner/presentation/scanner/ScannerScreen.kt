package com.cherishmashankar.scanora.feature.scanner.presentation.scanner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.feature.scanner.presentation.result.ScanResultContent
import com.cherishmashankar.scanora.feature.scanner.presentation.result.isBrowsableUrl
import com.cherishmashankar.scanora.feature.scanner.presentation.scanner.components.CameraContent
import com.cherishmashankar.scanora.feature.scanner.presentation.scanner.components.CameraErrorContent
import com.cherishmashankar.scanora.feature.scanner.presentation.scanner.components.PermissionContent

@Composable
internal fun ScannerScreen(
    uiState: ScannerUiState,
    onBarcodeDetected: (Scan) -> Unit,
    onCodeDetected: () -> Unit,
    onToggleFlash: () -> Unit,
    onSave: () -> Unit,
    onShare: (String) -> Unit,
    onBrowse: (String) -> Unit,
    onScanAgain: () -> Unit,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
    onCameraError: () -> Unit,
    onRetryCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (uiState.cameraPermissionState) {
            CameraPermissionState.UNKNOWN -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            CameraPermissionState.DENIED,
            CameraPermissionState.PERMANENTLY_DENIED -> {
                PermissionContent(
                    permissionState = uiState.cameraPermissionState,
                    onRequestPermission = onRequestPermission,
                    onOpenSettings = onOpenSettings
                )
            }

            CameraPermissionState.GRANTED -> {
                when {
                    uiState.cameraError != null -> {
                        CameraErrorContent(
                            onRetry = onRetryCamera,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    uiState.scanResult != null -> {
                        val scan = uiState.scanResult

                        ScanResultContent(
                            scan = scan,
                            showSaveAction = true,
                            showDeleteAction = false,
                            isSaved = uiState.isSaved,
                            canBrowse = scan.value.isBrowsableUrl(),
                            onSave = onSave,
                            onDelete = {},
                            onShare = { onShare(scan.value) },
                            onBrowse = { onBrowse(scan.value) },
                            onBottomAction = onScanAgain,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    else -> {
                        CameraContent(
                            isScanning = uiState.isScanning,
                            isFlashEnabled = uiState.isFlashEnabled,
                            scannerStatus = uiState.scannerStatus,
                            onCodeDetected = onCodeDetected,
                            onBarcodeDetected = onBarcodeDetected,
                            onToggleFlash = onToggleFlash,
                            onCameraError = onCameraError,
                            cameraRetryKey = uiState.cameraRetryKey,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}