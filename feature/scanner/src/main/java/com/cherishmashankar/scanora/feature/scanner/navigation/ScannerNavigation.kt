package com.cherishmashankar.scanora.feature.scanner.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cherishmashankar.scanora.feature.scanner.presentation.scanner.ScannerRoute
import com.cherishmashankar.scanora.feature.scanner.presentation.result.ScanResultRoute

const val SCANNER_ROUTE = "scanner"

private const val SCAN_RESULT_ROUTE = "scan_result/{scanId}"

fun NavHostController.navigateToScanResult(
    scanId: Long
) {
    navigate("scan_result/$scanId")
}

fun NavGraphBuilder.scannerScreen() {
    composable(
        route = SCANNER_ROUTE
    ) {
        ScannerRoute()
    }
}

fun NavGraphBuilder.scanResultScreen(
    onBack: () -> Unit
) {
    composable(
        route = SCAN_RESULT_ROUTE,
        arguments = listOf(
            navArgument("scanId") {
                type = NavType.LongType
            }
        )
    ) {
        ScanResultRoute(
            onBack = onBack
        )
    }
}