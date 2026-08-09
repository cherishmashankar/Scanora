package com.cherishmashankar.scanora.feature.scanner.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cherishmashankar.scanora.feature.scanner.presentation.result.isBrowsableUrl
import com.cherishmashankar.scanora.feature.scanner.presentation.result.toBrowsableUrl

@Composable
internal fun ScannerRoute(
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
        },

        onSave = {
            viewModel.onEvent(
                ScannerEvent.SaveScan
            )
        },

        onScanAgain = {
            viewModel.onEvent(
                ScannerEvent.ScanAgain
            )
        },

        onShare = { value ->
            val shareIntent =
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        value
                    )
                }

            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    null
                )
            )
        },

        onBrowse = { value ->
            if (value.isBrowsableUrl()) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(value.toBrowsableUrl())
                )

                context.startActivity(intent)
            }
        }
    )
}