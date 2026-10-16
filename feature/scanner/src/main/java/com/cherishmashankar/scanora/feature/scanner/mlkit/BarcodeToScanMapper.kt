package com.cherishmashankar.scanora.feature.scanner.mlkit

import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.core.model.ScanFormat
import com.google.mlkit.vision.barcode.common.Barcode

internal fun Barcode.toScan(): Scan? {
    val value = rawValue ?: return null

    return Scan(
        value = value,
        format = if (format == Barcode.FORMAT_QR_CODE) {
            ScanFormat.QR_CODE
        } else {
            ScanFormat.BARCODE
        },
        timestamp = System.currentTimeMillis()
    )
}