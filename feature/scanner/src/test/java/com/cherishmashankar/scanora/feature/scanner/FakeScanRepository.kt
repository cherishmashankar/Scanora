package com.cherishmashankar.scanora.feature.scanner

import com.cherishmashankar.scanora.core.data.repository.ScanRepository
import com.cherishmashankar.scanora.core.model.Scan
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

internal class FakeScanRepository : ScanRepository {

    val scanFlow = MutableStateFlow<Scan?>(null)

    var saveResult: Result<Long> = Result.success(1L)
    var deleteResult: Result<Unit> = Result.success(Unit)

    var saveCallCount = 0
    var deleteCallCount = 0

    var blockDelete = false
    val deleteGate = CompletableDeferred<Unit>()

    override fun observeScans(): Flow<List<Scan>> =
        flowOf(emptyList())

    override fun observeScan(id: Long): Flow<Scan?> =
        scanFlow

    override fun searchScans(query: String): Flow<List<Scan>> =
        flowOf(emptyList())

    override fun observeFavoriteScans(): Flow<List<Scan>> =
        flowOf(emptyList())

    override suspend fun saveScan(scan: Scan): Long {
        saveCallCount++
        return saveResult.getOrThrow()
    }

    override suspend fun deleteScan(id: Long) {
        deleteCallCount++

        if (blockDelete) {
            deleteGate.await()
        }

        deleteResult.getOrThrow()
    }

    override suspend fun updateFavorite(
        id: Long,
        isFavorite: Boolean
    ) = Unit
}