package com.cherishmashankar.scanora.feature.scanner.presentation

import androidx.annotation.StringRes
import com.cherishmashankar.scanora.core.model.ScanFormat
import com.cherishmashankar.scanora.feature.scanner.R

@StringRes
internal fun ScanFormat.toStringRes(): Int =
    when (this) {
        ScanFormat.QR_CODE -> R.string.scan_type_qr
        ScanFormat.BARCODE -> R.string.scan_type_barcode
    }