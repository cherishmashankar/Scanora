package com.cherishmashankar.scanora.feature.history.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.feature.history.presentation.components.HistoryEmptyContent
import com.cherishmashankar.scanora.feature.history.presentation.components.HistoryHeader
import com.cherishmashankar.scanora.feature.history.presentation.components.HistoryItemCard

@Composable
internal fun HistoryScreen(
    uiState: HistoryUiState,
    onScanClick: (Long) -> Unit,
    onSearchClick: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onCloseSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        HistoryHeader(
            isSearchActive = uiState.isSearchActive,
            searchQuery = uiState.searchQuery,
            onSearchClick = onSearchClick,
            onSearchQueryChanged = onSearchQueryChanged,
            onCloseSearch = onCloseSearch
        )

        if (uiState.scans.isEmpty()) {
            HistoryEmptyContent(
                modifier = Modifier.fillMaxSize()
            )

            return
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = uiState.scans,
                key = { scan -> scan.id }
            ) { scan ->
                HistoryItemCard(
                    scan = scan,
                    onClick = {
                        onScanClick(scan.id)
                    }
                )
            }
        }
    }
}