package com.cherishmashankar.scanora.feature.scanner.presentation

import com.cherishmashankar.scanora.core.model.Scan

internal data class ScannerUiState(
    val hasCameraPermission: Boolean = false,
    val isScanning: Boolean = true,
    val isFlashEnabled: Boolean = false,
    val scanResult: Scan? = null,
    val scannerStatus: ScannerStatus = ScannerStatus.INITIALIZING,
    val isSaved: Boolean = false
)