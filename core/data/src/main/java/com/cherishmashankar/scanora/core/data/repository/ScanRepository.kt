package com.cherishmashankar.scanora.core.data.repository

import com.cherishmashankar.scanora.core.model.Scan
import kotlinx.coroutines.flow.Flow

interface ScanRepository {

    fun observeScans(): Flow<List<Scan>>

    fun observeScan(id: Long): Flow<Scan?>

    fun observeFavoriteScans(): Flow<List<Scan>>

    suspend fun saveScan(scan: Scan): Long

    suspend fun updateFavorite(
        id: Long,
        isFavorite: Boolean
    )

    suspend fun deleteScan(id: Long)

    fun searchScans(query: String): Flow<List<Scan>>
}