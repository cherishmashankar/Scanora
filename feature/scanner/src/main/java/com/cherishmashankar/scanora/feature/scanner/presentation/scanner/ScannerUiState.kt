package com.cherishmashankar.scanora.feature.scanner.presentation.scanner

import com.cherishmashankar.scanora.core.model.Scan

internal data class ScannerUiState(
    val cameraPermissionState: CameraPermissionState =
        CameraPermissionState.UNKNOWN,
    val isScanning: Boolean = true,
    val isFlashEnabled: Boolean = false,
    val scanResult: Scan? = null,
    val scannerStatus: ScannerStatus = ScannerStatus.INITIALIZING,
    val isSaved: Boolean = false,
    val isSaving: Boolean = false,
    val saveError: ScannerError? = null
)

internal enum class ScannerError {
    SaveFailed
}

internal enum class CameraPermissionState {
    UNKNOWN,
    GRANTED,
    DENIED,
    PERMANENTLY_DENIED
}