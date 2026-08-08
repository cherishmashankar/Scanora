package com.cherishmashankar.scanora.feature.scanner.presentation

import com.cherishmashankar.scanora.core.model.Scan

internal sealed interface ScannerEvent {

    data class BarcodeDetected(
        val scan: Scan
    ) : ScannerEvent

    data object ScanAgain : ScannerEvent

    data object ToggleFlash : ScannerEvent

    data class CameraPermissionChanged(
        val granted: Boolean
    ) : ScannerEvent

    data object Detecting : ScannerEvent

    data object ScanResultConsumed : ScannerEvent
}