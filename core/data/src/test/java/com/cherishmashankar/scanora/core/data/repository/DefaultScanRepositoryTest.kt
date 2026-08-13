package com.cherishmashankar.scanora.core.data.repository

import com.cherishmashankar.scanora.core.database.dao.ScanDao
import com.cherishmashankar.scanora.core.database.entity.ScanEntity
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.core.model.ScanFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

internal class DefaultScanRepositoryTest {

    private val dao = FakeScanDao()
    private val repository = DefaultScanRepository(dao)

    @Test
    fun `save scan returns dao id`() = runTest {
        dao.insertResult = 42L

        val result = repository.saveScan(testScan())

        assertEquals(42L, result)
        assertEquals(1, dao.insertCallCount)
    }

    @Test
    fun `save scan maps model to entity`() = runTest {
        repository.saveScan(
            testScan(
                id = 7L,
                value = "hello",
                format = ScanFormat.BARCODE,
                isFavorite = true
            )
        )

        val entity = dao.lastInsertedScan

        assertEquals(7L, entity?.id)
        assertEquals("hello", entity?.value)
        assertEquals("BARCODE", entity?.format)
        assertEquals(true, entity?.isFavorite)
    }

    @Test
    fun `delete scan delegates correct id`() = runTest {
        repository.deleteScan(25L)

        assertEquals(1, dao.deleteCallCount)
        assertEquals(25L, dao.lastDeletedId)
    }

    @Test
    fun `observe scans maps entities to models`() = runTest {
        dao.scansFlow.value = listOf(
            testEntity(1L, "one"),
            testEntity(2L, "two", format = "BARCODE")
        )

        val result = repository.observeScans().first()

        assertEquals(2, result.size)
        assertEquals(1L, result[0].id)
        assertEquals("one", result[0].value)
        assertEquals(ScanFormat.QR_CODE, result[0].format)
        assertEquals(ScanFormat.BARCODE, result[1].format)
    }

    @Test
    fun `observe scans returns empty list when dao empty`() = runTest {
        dao.scansFlow.value = emptyList()

        val result = repository.observeScans().first()

        assertEquals(emptyList<Scan>(), result)
    }

    @Test
    fun `observe scan maps entity`() = runTest {
        dao.scanFlow.value = testEntity(
            id = 10L,
            value = "single"
        )

        val result = repository.observeScan(10L).first()

        assertEquals(10L, result?.id)
        assertEquals("single", result?.value)
    }

    @Test
    fun `observe scan returns null when dao returns null`() = runTest {
        dao.scanFlow.value = null

        val result = repository.observeScan(99L).first()

        assertNull(result)
    }

    @Test
    fun `search scans maps results`() = runTest {
        dao.searchFlow.value = listOf(
            testEntity(
                id = 5L,
                value = "https://google.com"
            )
        )

        val result = repository.searchScans("google").first()

        assertEquals(1, result.size)
        assertEquals("https://google.com", result.first().value)
    }

    @Test
    fun `search scans returns empty list when no match`() = runTest {
        dao.searchFlow.value = emptyList()

        val result = repository.searchScans("missing").first()

        assertEquals(emptyList<Scan>(), result)
    }

    @Test
    fun `observe favorites maps dao results`() = runTest {
        dao.favoriteScansFlow.value = listOf(
            testEntity(
                id = 3L,
                value = "favorite",
                isFavorite = true
            )
        )

        val result = repository.observeFavoriteScans().first()

        assertEquals(1, result.size)
        assertEquals(true, result.first().isFavorite)
    }

    @Test
    fun `update favorite delegates values to dao`() = runTest {
        repository.updateFavorite(
            id = 8L,
            isFavorite = true
        )

        assertEquals(8L, dao.lastFavoriteId)
        assertEquals(true, dao.lastFavoriteValue)
    }

    private fun testScan(
        id: Long = 0L,
        value: String = "https://example.com",
        format: ScanFormat = ScanFormat.QR_CODE,
        isFavorite: Boolean = false
    ) = Scan(
        id = id,
        value = value,
        format = format,
        timestamp = 100L,
        isFavorite = isFavorite
    )

    private fun testEntity(
        id: Long,
        value: String,
        format: String = "QR_CODE",
        isFavorite: Boolean = false
    ) = ScanEntity(
        id = id,
        value = value,
        format = format,
        timestamp = 100L,
        isFavorite = isFavorite
    )
}

private class FakeScanDao : ScanDao {

    val scansFlow = MutableStateFlow<List<ScanEntity>>(emptyList())
    val scanFlow = MutableStateFlow<ScanEntity?>(null)
    val searchFlow = MutableStateFlow<List<ScanEntity>>(emptyList())
    val favoriteScansFlow = MutableStateFlow<List<ScanEntity>>(emptyList())

    var insertResult = 1L
    var insertCallCount = 0
    var deleteCallCount = 0

    var lastInsertedScan: ScanEntity? = null
    var lastDeletedId: Long? = null
    var lastFavoriteId: Long? = null
    var lastFavoriteValue: Boolean? = null

    override fun observeScans(): Flow<List<ScanEntity>> = scansFlow

    override fun observeScan(id: Long): Flow<ScanEntity?> = scanFlow

    override fun searchScans(query: String): Flow<List<ScanEntity>> = searchFlow

    override fun observeFavoriteScans(): Flow<List<ScanEntity>> =
        favoriteScansFlow

    override suspend fun insertScan(scan: ScanEntity): Long {
        insertCallCount++
        lastInsertedScan = scan
        return insertResult
    }

    override suspend fun deleteScan(id: Long) {
        deleteCallCount++
        lastDeletedId = id
    }

    override suspend fun updateFavorite(
        id: Long,
        isFavorite: Boolean
    ) {
        lastFavoriteId = id
        lastFavoriteValue = isFavorite
    }
}