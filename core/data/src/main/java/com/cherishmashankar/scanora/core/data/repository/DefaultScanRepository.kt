package com.cherishmashankar.scanora.core.data.repository

import com.cherishmashankar.scanora.core.data.mapper.toEntity
import com.cherishmashankar.scanora.core.data.mapper.toModel
import com.cherishmashankar.scanora.core.database.dao.ScanDao
import com.cherishmashankar.scanora.core.model.Scan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultScanRepository @Inject constructor(
    private val scanDao: ScanDao
) : ScanRepository {

    override fun observeScans(): Flow<List<Scan>> =
        scanDao.observeScans()
            .map { entities ->
                entities.map { it.toModel() }
            }

    override fun observeScan(id: Long): Flow<Scan?> =
        scanDao.observeScan(id)
            .map { entity ->
                entity?.toModel()
            }

    override fun observeFavoriteScans(): Flow<List<Scan>> =
        scanDao.observeFavoriteScans()
            .map { entities ->
                entities.map { it.toModel() }
            }

    override suspend fun saveScan(scan: Scan): Long =
        scanDao.insertScan(scan.toEntity())

    override suspend fun updateFavorite(
        id: Long,
        isFavorite: Boolean
    ) {
        scanDao.updateFavorite(
            id = id,
            isFavorite = isFavorite
        )
    }

    override suspend fun deleteScan(id: Long) {
        scanDao.deleteScan(id)
    }

    override fun searchScans(
        query: String
    ): Flow<List<Scan>> =
        scanDao.searchScans(query)
            .map { entities ->
                entities.map { it.toModel() }
            }
}