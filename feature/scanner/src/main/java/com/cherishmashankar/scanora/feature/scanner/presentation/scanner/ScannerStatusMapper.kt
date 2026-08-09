package com.cherishmashankar.scanora.feature.scanner.presentation.scanner

import androidx.annotation.StringRes
import com.cherishmashankar.scanora.feature.scanner.R

@StringRes
internal fun ScannerStatus.toStringRes(): Int =
    when (this) {
        ScannerStatus.INITIALIZING -> R.string.scanner_initializing
        ScannerStatus.READY -> R.string.scanner_ready
        ScannerStatus.DETECTING -> R.string.scanner_detecting
        ScannerStatus.SUCCESS -> R.string.scanner_success
        ScannerStatus.ERROR -> R.string.scanner_error
    }