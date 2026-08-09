package com.cherishmashankar.scanora.feature.scanner.presentation.result

import com.cherishmashankar.scanora.core.model.Scan

internal sealed interface ScanResultUiState {

    data object Loading : ScanResultUiState

    data class Success(
        val scan: Scan
    ) : ScanResultUiState

    data object NotFound : ScanResultUiState
}

