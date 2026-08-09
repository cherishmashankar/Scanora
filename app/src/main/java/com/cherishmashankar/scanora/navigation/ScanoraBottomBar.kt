package com.cherishmashankar.scanora.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun ScanoraBottomBar(
    currentRoute: String?,
    onDestinationClick: (TopLevelDestination) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 6.dp
    ) {
        TopLevelDestination.entries.forEach { destination ->

            val isSelected =
                currentRoute == destination.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onDestinationClick(destination)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor =
                        MaterialTheme.colorScheme.onPrimaryContainer,

                    selectedTextColor =
                        MaterialTheme.colorScheme.primary,

                    indicatorColor =
                        MaterialTheme.colorScheme.primaryContainer,

                    unselectedIconColor =
                        MaterialTheme.colorScheme.onSurfaceVariant,

                    unselectedTextColor =
                        MaterialTheme.colorScheme.onSurfaceVariant
                ),
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(
                            destination.labelRes
                        )
                    )
                },
//                label = {
//                    Text(
//                        text = stringResource(
//                            destination.labelRes
//                        ),
//                        style = MaterialTheme.typography.labelMedium
//                    )
//                }
            )
        }
    }
}