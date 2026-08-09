package com.cherishmashankar.scanora.feature.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cherishmashankar.scanora.core.data.repository.ScanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class HistoryViewModel @Inject constructor(
    private val scanRepository: ScanRepository
) : ViewModel() {

    private val searchQuery =
        MutableStateFlow("")

    private val isSearchActive =
        MutableStateFlow(false)

    private val scans =
        searchQuery.flatMapLatest { query ->
            if (query.isBlank()) {
                scanRepository.observeScans()
            } else {
                scanRepository.searchScans(query)
            }
        }

    val uiState: StateFlow<HistoryUiState> =
        combine(
            searchQuery,
            isSearchActive,
            scans
        ) { query, searchActive, scans ->
            HistoryUiState(
                scans = scans,
                searchQuery = query,
                isSearchActive = searchActive
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HistoryUiState()
        )

    fun onSearchQueryChanged(
        query: String
    ) {
        searchQuery.value = query
    }

    fun onSearchClick() {
        isSearchActive.value = true
    }

    fun onCloseSearch() {
        searchQuery.value = ""
        isSearchActive.value = false
    }
}