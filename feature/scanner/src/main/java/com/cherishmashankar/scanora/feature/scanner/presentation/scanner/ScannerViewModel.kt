package com.cherishmashankar.scanora.feature.scanner.presentation.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cherishmashankar.scanora.core.data.repository.ScanRepository
import com.cherishmashankar.scanora.core.model.Scan
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class ScannerViewModel @Inject constructor(
    private val scanRepository: ScanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())

    val uiState: StateFlow<ScannerUiState> =
        _uiState.asStateFlow()

    fun onEvent(event: ScannerEvent) {
        when (event) {
            is ScannerEvent.BarcodeDetected -> {
                onBarcodeDetected(event.scan)
            }

            ScannerEvent.Detecting -> {
                if (!_uiState.value.isScanning) return

                _uiState.update {
                    it.copy(
                        scannerStatus = ScannerStatus.DETECTING
                    )
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
                    it.copy(
                        saveError = null
                    )
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
                cameraError = null
            )
        }
    }
}