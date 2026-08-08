package com.cherishmashankar.scanora.feature.scanner.presentation

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
                saveScan(event.scan)
            }

            ScannerEvent.Detecting -> {
                if (!_uiState.value.isScanning) return

                _uiState.update {
                    it.copy(
                        scannerStatus = ScannerStatus.DETECTING
                    )
                }
            }

            ScannerEvent.ScanAgain -> {
                _uiState.update {
                    it.copy(
                        isScanning = true,
                        scanResult = null,
                        scannerStatus = ScannerStatus.READY
                    )
                }
            }

            ScannerEvent.ToggleFlash -> {
                _uiState.update {
                    it.copy(
                        isFlashEnabled = !it.isFlashEnabled
                    )
                }
            }

            ScannerEvent.ScanResultConsumed -> {
                _uiState.update {
                    it.copy(
                        savedScanId = null,
                        scanResult = null,
                        isScanning = true,
                        scannerStatus = ScannerStatus.READY
                    )
                }
            }

            is ScannerEvent.CameraPermissionChanged -> {
                _uiState.update {
                    it.copy(
                        hasCameraPermission = event.granted,
                        scannerStatus = if (event.granted) {
                            ScannerStatus.READY
                        } else {
                            ScannerStatus.ERROR
                        }
                    )
                }
            }
        }
    }

    private fun saveScan(scan: Scan) {
        if (!_uiState.value.isScanning) return

        _uiState.update {
            it.copy(isScanning = false)
        }

        viewModelScope.launch {
            runCatching {
                scanRepository.saveScan(scan)
            }.onSuccess { id ->
                _uiState.update {
                    it.copy(
                        scanResult = scan.copy(id = id),
                        savedScanId = id,
                        scannerStatus = ScannerStatus.SUCCESS
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        isScanning = true,
                        scannerStatus = ScannerStatus.READY
                    )
                }
            }
        }
    }
}