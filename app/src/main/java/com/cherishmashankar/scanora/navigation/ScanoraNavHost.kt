package com.cherishmashankar.scanora.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cherishmashankar.scanora.feature.history.navigation.HISTORY_ROUTE
import com.cherishmashankar.scanora.feature.history.navigation.historyScreen
import com.cherishmashankar.scanora.feature.scanner.navigation.SCANNER_ROUTE
import com.cherishmashankar.scanora.feature.scanner.navigation.navigateToScanResult
import com.cherishmashankar.scanora.feature.scanner.navigation.scanResultScreen
import com.cherishmashankar.scanora.feature.scanner.navigation.scannerScreen

@Composable
fun ScanoraNavHost() {
    val navController = rememberNavController()

    val backStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        backStackEntry?.destination?.route

    val showBottomBar =
        currentRoute == SCANNER_ROUTE ||
                currentRoute == HISTORY_ROUTE

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                ScanoraBottomBar(
                    currentRoute = currentRoute,
                    onDestinationClick = { destination ->
                        navController.navigate(destination.route) {
                            popUpTo(SCANNER_ROUTE) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = SCANNER_ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            scannerScreen()

            historyScreen(
                onScanClick = { scanId ->
                    navController.navigateToScanResult(scanId)
                }
            )

            scanResultScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}