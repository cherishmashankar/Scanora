package com.cherishmashankar.scanora.feature.scanner.presentation.scanner.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.feature.scanner.R
import com.cherishmashankar.scanora.feature.scanner.presentation.scanner.CameraPermissionState

@Composable
internal fun PermissionContent(
    permissionState: CameraPermissionState,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(
                R.string.camera_permission_required
            ),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        when (permissionState) {
            CameraPermissionState.PERMANENTLY_DENIED -> {
                Button(
                    onClick = onOpenSettings
                ) {
                    Text(
                        text = stringResource(
                            R.string.open_settings
                        )
                    )
                }
            }

            else -> {
                Button(
                    onClick = onRequestPermission
                ) {
                    Text(
                        text = stringResource(
                            R.string.grant_permission
                        )
                    )
                }
            }
        }
    }
}