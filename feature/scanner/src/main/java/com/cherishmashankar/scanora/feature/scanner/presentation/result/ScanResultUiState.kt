package com.cherishmashankar.scanora.feature.scanner.presentation.result

import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.core.model.ScanContent

sealed interface ScanResultUiState {
    data object Loading : ScanResultUiState
    data object NotFound : ScanResultUiState

    data class Success(
        val scan: Scan,
        val content: ScanContent,
        val actions: List<SmartAction>
    ) : ScanResultUiState
}