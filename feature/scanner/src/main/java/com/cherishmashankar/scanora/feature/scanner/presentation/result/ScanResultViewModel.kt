package com.cherishmashankar.scanora.feature.scanner.presentation.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cherishmashankar.scanora.core.data.repository.ScanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class ScanResultViewModel @Inject constructor(
    private val scanRepository: ScanRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val scanId: Long? =
        savedStateHandle["scanId"]

    private val _isDeleting =
        MutableStateFlow(false)

    val isDeleting: StateFlow<Boolean> =
        _isDeleting

    private val _deleteError =
        MutableStateFlow(false)

    val deleteError: StateFlow<Boolean> =
        _deleteError

    val uiState: StateFlow<ScanResultUiState> =
        if (scanId == null) {
            MutableStateFlow(
                ScanResultUiState.NotFound
            )
        } else {
            scanRepository.observeScan(scanId)
                .map { scan ->
                    if (scan != null) {
                        ScanResultUiState.Success(scan)
                    } else {
                        ScanResultUiState.NotFound
                    }
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = ScanResultUiState.Loading
                )
        }

    fun deleteScan(
        onDeleted: () -> Unit
    ) {
        val id = scanId ?: return

        if (_isDeleting.value) return

        _isDeleting.value = true
        _deleteError.value = false

        viewModelScope.launch {
            runCatching {
                scanRepository.deleteScan(id)
            }.onSuccess {
                _isDeleting.value = false
                onDeleted()
            }.onFailure {
                _isDeleting.value = false
                _deleteError.value = true
            }
        }
    }

    fun consumeDeleteError() {
        _deleteError.value = false
    }
}