package com.cherishmashankar.scanora.feature.scanner.presentation.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cherishmashankar.scanora.core.data.repository.ScanRepository
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.feature.scanner.presentation.ScannerEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ScannerViewModel @Inject constructor(
    private val scanRepository: ScanRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(ScannerUiState())

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

        val scan = currentState.scanResult
            ?: return

        // Prevent multiple taps creating duplicate rows.
        if (currentState.isSaved) return

        viewModelScope.launch {
            runCatching {
                scanRepository.saveScan(scan)
            }.onSuccess { id ->

                _uiState.update {
                    it.copy(
                        scanResult = scan.copy(id = id),
                        isSaved = true
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
                isSaved = false
            )
        }
    }
}