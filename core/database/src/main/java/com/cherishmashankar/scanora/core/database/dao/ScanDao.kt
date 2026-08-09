package com.cherishmashankar.scanora.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cherishmashankar.scanora.core.database.entity.ScanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: ScanEntity): Long

    @Query(
        """
        SELECT * 
        FROM scans
        ORDER BY timestamp DESC
        """
    )
    fun observeScans(): Flow<List<ScanEntity>>

    @Query(
        """
        SELECT * 
        FROM scans
        WHERE id = :id
        LIMIT 1
        """
    )
    fun observeScan(id: Long): Flow<ScanEntity?>

    @Query(
        """
        SELECT *
        FROM scans
        WHERE isFavorite = 1
        ORDER BY timestamp DESC
        """
    )
    fun observeFavoriteScans(): Flow<List<ScanEntity>>

    @Query(
        """
        UPDATE scans
        SET isFavorite = :isFavorite
        WHERE id = :id
        """
    )
    suspend fun updateFavorite(
        id: Long,
        isFavorite: Boolean
    )

    @Query(
        """
    DELETE FROM scans
    WHERE id = :id
    """
    )
    suspend fun deleteScan(id: Long)

    @Query(
        """
    SELECT *
    FROM scans
    WHERE value LIKE '%' || :query || '%'
    ORDER BY timestamp DESC
    """
    )
    fun searchScans(query: String): Flow<List<ScanEntity>>

}