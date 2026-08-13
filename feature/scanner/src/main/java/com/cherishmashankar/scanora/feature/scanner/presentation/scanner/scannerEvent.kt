package com.cherishmashankar.scanora.feature.scanner.presentation.scanner

import androidx.annotation.StringRes
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.core.model.ScanFormat
import com.cherishmashankar.scanora.feature.scanner.R

@StringRes
internal fun ScanFormat.toStringRes(): Int =
    when (this) {
        ScanFormat.QR_CODE -> R.string.scan_type_qr
        ScanFormat.BARCODE -> R.string.scan_type_barcode
    }

internal sealed interface ScannerEvent {

    data class BarcodeDetected(
        val scan: Scan
    ) : ScannerEvent

    data object Detecting : ScannerEvent

    data object SaveScan : ScannerEvent

    data object ScanAgain : ScannerEvent

    data object ToggleFlash : ScannerEvent

    data class CameraPermissionChanged(
        val state: CameraPermissionState
    ) : ScannerEvent

    data object SaveErrorConsumed : ScannerEvent

    data object CameraFailed : ScannerEvent

    data object RetryCamera : ScannerEvent
}
