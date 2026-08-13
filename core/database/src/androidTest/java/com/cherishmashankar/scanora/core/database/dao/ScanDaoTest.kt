package com.cherishmashankar.scanora.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cherishmashankar.scanora.core.database.ScanoraDatabase
import com.cherishmashankar.scanora.core.database.entity.ScanEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class ScanDaoTest {

    private lateinit var database: ScanoraDatabase
    private lateinit var dao: ScanDao

    @Before
    fun setup() {
        val context =
            ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            ScanoraDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.scanDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndObserveScan() = runTest {
        val id = dao.insertScan(
            testEntity(
                value = "https://example.com",
                timestamp = 100L
            )
        )

        val result = dao.observeScan(id).first()

        assertEquals("https://example.com", result?.value)
        assertEquals("QR_CODE", result?.format)
    }

    @Test
    fun observeScansReturnsNewestFirst() = runTest {
        dao.insertScan(testEntity("old", 100L))
        dao.insertScan(testEntity("new", 300L))
        dao.insertScan(testEntity("middle", 200L))

        val result = dao.observeScans().first()

        assertEquals(3, result.size)
        assertEquals("new", result[0].value)
        assertEquals("middle", result[1].value)
        assertEquals("old", result[2].value)
    }

    @Test
    fun searchScansReturnsMatchingValues() = runTest {
        dao.insertScan(
            testEntity(
                value = "https://google.com",
                timestamp = 100L
            )
        )
        dao.insertScan(
            testEntity(
                value = "https://example.com",
                timestamp = 200L
            )
        )

        val result = dao.searchScans("google").first()

        assertEquals(1, result.size)
        assertEquals("https://google.com", result.first().value)
    }

    @Test
    fun searchScansIsCaseInsensitive() = runTest {
        dao.insertScan(
            testEntity(
                value = "HTTPS://GOOGLE.COM",
                timestamp = 100L
            )
        )

        val result = dao.searchScans("google").first()

        assertEquals(1, result.size)
    }

    @Test
    fun searchScansReturnsEmptyWhenNothingMatches() = runTest {
        dao.insertScan(
            testEntity(
                value = "Scanora",
                timestamp = 100L
            )
        )

        val result = dao.searchScans("missing").first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun observeUnknownScanReturnsNull() = runTest {
        val result = dao.observeScan(999L).first()

        assertNull(result)
    }

    @Test
    fun deleteScanRemovesItem() = runTest {
        val id = dao.insertScan(
            testEntity(
                value = "delete me",
                timestamp = 100L
            )
        )

        dao.deleteScan(id)

        val result = dao.observeScan(id).first()

        assertNull(result)
    }

    @Test
    fun deletingOneScanDoesNotDeleteOthers() = runTest {
        val firstId = dao.insertScan(
            testEntity(
                value = "first",
                timestamp = 100L
            )
        )

        dao.insertScan(
            testEntity(
                value = "second",
                timestamp = 200L
            )
        )

        dao.deleteScan(firstId)

        val result = dao.observeScans().first()

        assertEquals(1, result.size)
        assertEquals("second", result.first().value)
    }

    @Test
    fun deletingUnknownIdDoesNotAffectExistingRows() = runTest {
        dao.insertScan(
            testEntity(
                value = "keep",
                timestamp = 100L
            )
        )

        dao.deleteScan(999L)

        val result = dao.observeScans().first()

        assertEquals(1, result.size)
        assertEquals("keep", result.first().value)
    }

    @Test
    fun favoriteScansOnlyReturnsFavorites() = runTest {
        val favoriteId = dao.insertScan(
            testEntity(
                value = "favorite",
                timestamp = 100L,
                isFavorite = true
            )
        )

        dao.insertScan(
            testEntity(
                value = "normal",
                timestamp = 200L
            )
        )

        val result = dao.observeFavoriteScans().first()

        assertEquals(1, result.size)
        assertEquals(favoriteId, result.first().id)
        assertTrue(result.first().isFavorite)
    }

    @Test
    fun updateFavoriteChangesFavoriteState() = runTest {
        val id = dao.insertScan(
            testEntity(
                value = "scan",
                timestamp = 100L
            )
        )

        dao.updateFavorite(
            id = id,
            isFavorite = true
        )

        val result = dao.observeScan(id).first()

        assertEquals(true, result?.isFavorite)
    }

    private fun testEntity(
        value: String,
        timestamp: Long,
        isFavorite: Boolean = false
    ) = ScanEntity(
        id = 0L,
        value = value,
        format = "QR_CODE",
        timestamp = timestamp,
        isFavorite = isFavorite
    )
}