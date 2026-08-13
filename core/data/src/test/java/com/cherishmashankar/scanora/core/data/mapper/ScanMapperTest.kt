package com.cherishmashankar.scanora.core.data.mapper

import com.cherishmashankar.scanora.core.database.entity.ScanEntity
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.core.model.ScanFormat
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ScanMapperTest {

    @Test
    fun `qr entity maps to model`() {
        val entity = ScanEntity(
            id = 42L,
            value = "https://example.com",
            format = "QR_CODE",
            timestamp = 100L,
            isFavorite = false
        )

        val result = entity.toModel()

        assertEquals(42L, result.id)
        assertEquals("https://example.com", result.value)
        assertEquals(ScanFormat.QR_CODE, result.format)
        assertEquals(100L, result.timestamp)
        assertEquals(false, result.isFavorite)
    }

    @Test
    fun `barcode entity maps to model`() {
        val entity = ScanEntity(
            id = 7L,
            value = "1234567890123",
            format = "BARCODE",
            timestamp = 200L,
            isFavorite = true
        )

        val result = entity.toModel()

        assertEquals(7L, result.id)
        assertEquals("1234567890123", result.value)
        assertEquals(ScanFormat.BARCODE, result.format)
        assertEquals(200L, result.timestamp)
        assertEquals(true, result.isFavorite)
    }

    @Test
    fun `qr model maps to entity`() {
        val scan = Scan(
            id = 42L,
            value = "https://example.com",
            format = ScanFormat.QR_CODE,
            timestamp = 100L,
            isFavorite = false
        )

        val result = scan.toEntity()

        assertEquals(42L, result.id)
        assertEquals("https://example.com", result.value)
        assertEquals("QR_CODE", result.format)
        assertEquals(100L, result.timestamp)
        assertEquals(false, result.isFavorite)
    }

    @Test
    fun `barcode model maps to entity`() {
        val scan = Scan(
            id = 7L,
            value = "1234567890123",
            format = ScanFormat.BARCODE,
            timestamp = 200L,
            isFavorite = true
        )

        val result = scan.toEntity()

        assertEquals(7L, result.id)
        assertEquals("1234567890123", result.value)
        assertEquals("BARCODE", result.format)
        assertEquals(200L, result.timestamp)
        assertEquals(true, result.isFavorite)
    }

    @Test
    fun `model round trip preserves all fields`() {
        val scan = Scan(
            id = 99L,
            value = "Scanora",
            format = ScanFormat.QR_CODE,
            timestamp = 999L,
            isFavorite = true
        )

        val result = scan
            .toEntity()
            .toModel()

        assertEquals(scan, result)
    }

    @Test
    fun `entity round trip preserves all fields`() {
        val entity = ScanEntity(
            id = 88L,
            value = "987654321",
            format = "BARCODE",
            timestamp = 555L,
            isFavorite = false
        )

        val result = entity
            .toModel()
            .toEntity()

        assertEquals(entity, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `unknown format throws`() {
        ScanEntity(
            id = 1L,
            value = "test",
            format = "UNKNOWN",
            timestamp = 1L,
            isFavorite = false
        ).toModel()
    }
}