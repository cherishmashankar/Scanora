package com.cherishmashankar.scanora.feature.history.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.cherishmashankar.scanora.feature.history.presentation.HistoryRoute

const val HISTORY_ROUTE = "history"

fun NavHostController.navigateToHistory() {
    navigate(HISTORY_ROUTE)
}

fun NavGraphBuilder.historyScreen(
    onScanClick: (Long) -> Unit
) {
    composable(
        route = HISTORY_ROUTE
    ) {
        HistoryRoute(
            onScanClick = onScanClick
        )
    }
}