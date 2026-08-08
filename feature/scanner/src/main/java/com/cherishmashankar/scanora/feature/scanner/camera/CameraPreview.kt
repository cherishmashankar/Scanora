package com.cherishmashankar.scanora.feature.scanner.camera

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.core.model.ScanFormat
import com.cherishmashankar.scanora.feature.scanner.mlkit.createBarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode

@Composable
internal fun CameraPreview(
    isScanning: Boolean,
    isFlashEnabled: Boolean,
    onCodeDetected: () -> Unit,
    onBarcodeDetected: (Scan) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val latestOnCodeDetected = rememberUpdatedState(onCodeDetected)
    val latestOnBarcodeDetected = rememberUpdatedState(onBarcodeDetected)

    val barcodeScanner = remember {
        createBarcodeScanner()
    }

    val mainExecutor = remember {
        ContextCompat.getMainExecutor(context)
    }

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            setEnabledUseCases(
                LifecycleCameraController.IMAGE_ANALYSIS
            )

            setImageAnalysisBackpressureStrategy(
                ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
            )
        }
    }

    val mlKitAnalyzer = remember {
        MlKitAnalyzer(
            listOf(barcodeScanner),
            ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
            mainExecutor
        ) { result ->

            val barcodes = result
                ?.getValue(barcodeScanner)
                ?: emptyList()

            val barcode = barcodes.firstOrNull()
                ?: return@MlKitAnalyzer

            latestOnCodeDetected.value()

            val value = barcode.rawValue
                ?: return@MlKitAnalyzer

            val scan = Scan(
                value = value,
                format = if (barcode.format == Barcode.FORMAT_QR_CODE) {
                    ScanFormat.QR_CODE
                } else {
                    ScanFormat.BARCODE
                },
                timestamp = System.currentTimeMillis()
            )

            latestOnBarcodeDetected.value(scan)
        }
    }

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            controller = cameraController
        }
    }

    LaunchedEffect(lifecycleOwner) {
        cameraController.bindToLifecycle(lifecycleOwner)
    }

    LaunchedEffect(isFlashEnabled) {
        cameraController.enableTorch(isFlashEnabled)
    }

    LaunchedEffect(isScanning) {
        if (isScanning) {
            cameraController.setImageAnalysisAnalyzer(
                mainExecutor,
                mlKitAnalyzer
            )
        } else {
            cameraController.clearImageAnalysisAnalyzer()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraController.clearImageAnalysisAnalyzer()
            barcodeScanner.close()
        }
    }

    AndroidView(
        factory = {
            previewView
        },
        modifier = modifier.fillMaxSize()
    )
}