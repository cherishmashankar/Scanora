package com.cherishmashankar.scanora.feature.history.presentation

import com.cherishmashankar.scanora.core.data.repository.ScanRepository
import com.cherishmashankar.scanora.core.model.Scan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

internal class FakeScanRepository : ScanRepository {

    val scansFlow = MutableStateFlow<List<Scan>>(emptyList())
    val searchFlow = MutableStateFlow<List<Scan>>(emptyList())

    override fun observeScans(): Flow<List<Scan>> =
        scansFlow

    override fun searchScans(query: String): Flow<List<Scan>> =
        searchFlow

    override fun observeScan(id: Long): Flow<Scan?> =
        flowOf(null)

    override suspend fun saveScan(scan: Scan): Long =
        1L

    override suspend fun deleteScan(id: Long) = Unit

    override fun observeFavoriteScans(): Flow<List<Scan>> =
        flowOf(emptyList())

    override suspend fun updateFavorite(
        id: Long,
        isFavorite: Boolean
    ) = Unit
}