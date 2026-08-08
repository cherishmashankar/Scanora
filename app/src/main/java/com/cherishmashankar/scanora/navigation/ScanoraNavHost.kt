package com.cherishmashankar.scanora.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.cherishmashankar.scanora.feature.scanner.navigation.SCANNER_ROUTE
import com.cherishmashankar.scanora.feature.scanner.navigation.navigateToScanResult
import com.cherishmashankar.scanora.feature.scanner.navigation.scanResultScreen
import com.cherishmashankar.scanora.feature.scanner.navigation.scannerScreen

@Composable
fun ScanoraNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SCANNER_ROUTE
    ) {
        scannerScreen(
            onScanResult = { scanId ->
                navController.navigateToScanResult(scanId)
            }
        )

        scanResultScreen(
            onScanAgain = {
                navController.popBackStack()
            }
        )
    }
}