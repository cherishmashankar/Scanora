package com.cherishmashankar.scanora.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector
import com.cherishmashankar.scanora.R
import com.cherishmashankar.scanora.feature.history.navigation.HISTORY_ROUTE
import com.cherishmashankar.scanora.feature.scanner.navigation.SCANNER_ROUTE

enum class TopLevelDestination(
    val route: String,
    val icon: ImageVector,
    @param:StringRes val labelRes: Int
){
    SCANNER(
        route = SCANNER_ROUTE,
        icon = Icons.Default.QrCodeScanner,
        labelRes = R.string.nav_scanner
    ),

    HISTORY(
        route = HISTORY_ROUTE,
        icon = Icons.Default.History,
        labelRes = R.string.nav_history
    )
}