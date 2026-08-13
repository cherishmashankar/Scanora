package com.cherishmashankar.scanora.feature.scanner.presentation.scanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cherishmashankar.scanora.feature.scanner.R
import com.cherishmashankar.scanora.feature.scanner.presentation.result.isBrowsableUrl
import com.cherishmashankar.scanora.feature.scanner.presentation.result.toBrowsableUrl
import androidx.core.net.toUri

@Composable
internal fun ScannerRoute(
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val saveFailedMessage = stringResource(R.string.scan_save_failed)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        val state = when {
            granted -> CameraPermissionState.GRANTED
            activity?.shouldShowRequestPermissionRationale(
                Manifest.permission.CAMERA
            ) == true -> CameraPermissionState.DENIED
            else -> CameraPermissionState.PERMANENTLY_DENIED
        }

        viewModel.onEvent(
            ScannerEvent.CameraPermissionChanged(state)
        )
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        viewModel.onEvent(
            ScannerEvent.CameraPermissionChanged(
                if (granted) {
                    CameraPermissionState.GRANTED
                } else {
                    CameraPermissionState.DENIED
                }
            )
        )
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val granted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                if (granted) {
                    viewModel.onEvent(
                        ScannerEvent.CameraPermissionChanged(
                            CameraPermissionState.GRANTED
                        )
                    )
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(uiState.saveError) {
        when (uiState.saveError) {
            ScannerError.SaveFailed -> {
                snackbarHostState.showSnackbar(
                    message = saveFailedMessage
                )

                viewModel.onEvent(
                    ScannerEvent.SaveErrorConsumed
                )
            }

            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { innerPadding ->
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
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, value)
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
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            value.toBrowsableUrl().toUri()
                        )
                    )
                }
            },
            onRequestPermission = {
                permissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            },
            onOpenSettings = {
                context.startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts(
                            "package",
                            context.packageName,
                            null
                        )
                    )
                )
            },
            onCameraError = {
                viewModel.onEvent(
                    ScannerEvent.CameraFailed
                )
            },
            onRetryCamera = {
                viewModel.onEvent(
                    ScannerEvent.RetryCamera
                )
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}