package com.cherishmashankar.scanora.feature.scanner.presentation.scanner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.feature.scanner.presentation.result.ScanResultContent
import com.cherishmashankar.scanora.feature.scanner.presentation.result.isBrowsableUrl
import com.cherishmashankar.scanora.feature.scanner.presentation.scanner.components.CameraContent
import com.cherishmashankar.scanora.feature.scanner.presentation.scanner.components.CameraErrorContent
import com.cherishmashankar.scanora.feature.scanner.presentation.scanner.components.PermissionContent
import com.cherishmashankar.scanora.feature.scanner.R
import androidx.compose.foundation.layout.size


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
    onImportImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("scanner_screen")
    ) {
        val scan = uiState.scanResult

        if (scan != null) {
            // Shared result screen for camera and gallery scans
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
        } else {
            // Camera UI depends on camera permission
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
                    if (uiState.cameraError != null) {
                        CameraErrorContent(
                            onRetry = onRetryCamera,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        CameraContent(
                            isScanning = uiState.isScanning && !uiState.isGalleryScanning,
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


            Button(
                onClick = onImportImage,
                enabled = !uiState.isGalleryScanning,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
                    .testTag("import_image_button")
            ) {
                if (uiState.isGalleryScanning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(R.string.import_image))
                }
            }
        }
    }
}