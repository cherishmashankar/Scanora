package com.cherishmashankar.scanora.feature.history.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun HistoryRoute(
    onScanClick: (Long) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryScreen(
        uiState = uiState,
        onScanClick = onScanClick,
        onSearchClick = viewModel::onSearchClick,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onCloseSearch = viewModel::onCloseSearch
    )
}