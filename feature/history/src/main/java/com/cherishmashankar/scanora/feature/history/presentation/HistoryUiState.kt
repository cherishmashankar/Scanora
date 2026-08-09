package com.cherishmashankar.scanora.feature.history.presentation

import com.cherishmashankar.scanora.core.model.Scan

internal data class HistoryUiState(
    val scans: List<Scan> = emptyList(),
    val searchQuery: String = "",
    val isSearchActive: Boolean = false
)