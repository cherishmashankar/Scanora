package com.cherishmashankar.scanora.feature.scanner.presentation.scanner

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cherishmashankar.scanora.core.data.repository.ScanRepository
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.feature.scanner.mlkit.GalleryImageScanner
import com.cherishmashankar.scanora.feature.scanner.mlkit.toScan
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class ScannerViewModel @Inject constructor(
    private val scanRepository: ScanRepository,
    private val galleryImageScanner: GalleryImageScanner
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())

    val uiState: StateFlow<ScannerUiState> =
        _uiState.asStateFlow()

    fun onEvent(event: ScannerEvent) {
        when (event) {
            is ScannerEvent.BarcodeDetected -> {
                // Ignore camera results while processing a gallery image.
                if (!_uiState.value.isGalleryScanning) {
                    onBarcodeDetected(event.scan)
                }
            }

            is ScannerEvent.GalleryImageSelected -> {
                scanSelectedImage(event.uri)
            }

            ScannerEvent.GalleryErrorConsumed -> {
                _uiState.update {
                    it.copy(galleryError = null)
                }
            }

            ScannerEvent.Detecting -> {
                if (!_uiState.value.isScanning ||
                    _uiState.value.isGalleryScanning
                ) return

                _uiState.update {
                    it.copy(scannerStatus = ScannerStatus.DETECTING)
                }
            }

            ScannerEvent.SaveScan -> {
                saveCurrentScan()
            }

            ScannerEvent.ScanAgain -> {
                resetScanner()
            }

            ScannerEvent.ToggleFlash -> {
                _uiState.update {
                    it.copy(
                        isFlashEnabled = !it.isFlashEnabled
                    )
                }
            }

            ScannerEvent.SaveErrorConsumed -> {
                _uiState.update {
                    it.copy(saveError = null)
                }
            }

            ScannerEvent.CameraFailed -> {
                _uiState.update {
                    it.copy(
                        isScanning = false,
                        scannerStatus = ScannerStatus.ERROR,
                        cameraError = ScannerError.CameraUnavailable
                    )
                }
            }

            ScannerEvent.RetryCamera -> {
                _uiState.update {
                    it.copy(
                        isScanning = true,
                        scannerStatus = ScannerStatus.INITIALIZING,
                        cameraError = null,
                        cameraRetryKey = it.cameraRetryKey + 1
                    )
                }
            }

            is ScannerEvent.CameraPermissionChanged -> {
                _uiState.update {
                    it.copy(
                        cameraPermissionState = event.state,
                        scannerStatus = if (
                            event.state == CameraPermissionState.GRANTED
                        ) {
                            ScannerStatus.READY
                        } else {
                            ScannerStatus.ERROR
                        }
                    )
                }
            }
        }
    }

    private fun onBarcodeDetected(scan: Scan) {
        if (!_uiState.value.isScanning) return

        _uiState.update {
            it.copy(
                isScanning = false,
                scanResult = scan,
                scannerStatus = ScannerStatus.SUCCESS,
                isSaved = false
            )
        }
    }

    private fun scanSelectedImage(uri: Uri) {
        if (!_uiState.value.isScanning ||
            _uiState.value.isGalleryScanning
        ) return

        _uiState.update {
            it.copy(
                isGalleryScanning = true,
                galleryError = null
            )
        }

        viewModelScope.launch {
            try {
                val barcodes = galleryImageScanner.scan(uri)

                val scan = barcodes.firstNotNullOfOrNull { barcode ->
                    barcode.toScan()
                }

                if (scan != null) {
                    onBarcodeDetected(scan)
                } else {
                    _uiState.update {
                        it.copy(
                            galleryError = ScannerError.NoBarcodeFound
                        )
                    }
                }
            } catch (exception: Exception) {
                if (exception is CancellationException) {
                    throw exception
                }

                _uiState.update {
                    it.copy(
                        galleryError = ScannerError.GalleryScanFailed
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(isGalleryScanning = false)
                }
            }
        }
    }

    private fun saveCurrentScan() {
        val currentState = _uiState.value
        val scan = currentState.scanResult ?: return

        if (currentState.isSaved || currentState.isSaving) return

        _uiState.update {
            it.copy(
                isSaving = true,
                saveError = null
            )
        }

        viewModelScope.launch {
            runCatching {
                scanRepository.saveScan(scan)
            }.onSuccess { id ->
                _uiState.update {
                    it.copy(
                        scanResult = scan.copy(id = id),
                        isSaved = true,
                        isSaving = false,
                        saveError = null
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saveError = ScannerError.SaveFailed
                    )
                }
            }
        }
    }

    private fun resetScanner() {
        _uiState.update {
            it.copy(
                isScanning = true,
                scanResult = null,
                scannerStatus = ScannerStatus.READY,
                isSaved = false,
                isSaving = false,
                saveError = null,
                cameraError = null,
                galleryError = null
            )
        }
    }
}