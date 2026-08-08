package com.cherishmashankar.scanora.feature.scanner.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun ScannerRoute(
    onScanResult: (Long) -> Unit,
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            viewModel.onEvent(
                ScannerEvent.CameraPermissionChanged(granted)
            )
        }

    LaunchedEffect(Unit) {
        val granted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

        viewModel.onEvent(
            ScannerEvent.CameraPermissionChanged(granted)
        )

        if (!granted) {
            permissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }

    LaunchedEffect(uiState.savedScanId) {
        val scanId = uiState.savedScanId
            ?: return@LaunchedEffect

        onScanResult(scanId)

        viewModel.onEvent(
            ScannerEvent.ScanResultConsumed
        )
    }

    ScannerScreen(
        uiState = uiState,
        onCodeDetected = {
            viewModel.onEvent(
                ScannerEvent.Detecting
            )
        },
        onBarcodeDetected = { scan ->
            viewModel.onEvent(
                ScannerEvent.BarcodeDetected(scan)
            )
        },
        onToggleFlash = {
            viewModel.onEvent(
                ScannerEvent.ToggleFlash
            )
        }
    )
}