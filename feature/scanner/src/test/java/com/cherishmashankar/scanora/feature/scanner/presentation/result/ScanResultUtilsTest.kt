package com.cherishmashankar.scanora.feature.scanner.presentation.result

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class ScanResultUtilsTest {

    @Test
    fun `https url is browsable`() {
        assertTrue("https://example.com".isBrowsableUrl())
    }

    @Test
    fun `http url is browsable`() {
        assertTrue("http://example.com".isBrowsableUrl())
    }

    @Test
    fun `www url is browsable`() {
        assertTrue("www.example.com".isBrowsableUrl())
    }

    @Test
    fun `url check ignores case`() {
        assertTrue("HTTPS://EXAMPLE.COM".isBrowsableUrl())
        assertTrue("WWW.EXAMPLE.COM".isBrowsableUrl())
    }

    @Test
    fun `url check trims whitespace`() {
        assertTrue("   https://example.com   ".isBrowsableUrl())
    }

    @Test
    fun `plain text is not browsable`() {
        assertFalse("Hello Scanora".isBrowsableUrl())
    }

    @Test
    fun `barcode number is not browsable`() {
        assertFalse("1234567890123".isBrowsableUrl())
    }

    @Test
    fun `empty value is not browsable`() {
        assertFalse("".isBrowsableUrl())
    }

    @Test
    fun `whitespace value is not browsable`() {
        assertFalse("     ".isBrowsableUrl())
    }

    @Test
    fun `www url gets https scheme`() {
        assertEquals(
            "https://www.example.com",
            "www.example.com".toBrowsableUrl()
        )
    }

    @Test
    fun `https url remains unchanged`() {
        assertEquals(
            "https://example.com",
            "https://example.com".toBrowsableUrl()
        )
    }

    @Test
    fun `http url remains unchanged`() {
        assertEquals(
            "http://example.com",
            "http://example.com".toBrowsableUrl()
        )
    }

    @Test
    fun `normalizes surrounding whitespace`() {
        assertEquals(
            "https://www.example.com",
            "  www.example.com  ".toBrowsableUrl()
        )
    }

    // V2: Gallery image scanning edge cases

    @Test
    fun `gallery qr code containing https url is browsable`() {
        val scannedValue = "https://scanora.app"

        assertTrue(scannedValue.isBrowsableUrl())
    }

    @Test
    fun `gallery barcode containing numeric value is not browsable`() {
        val scannedValue = "5901234123457"

        assertFalse(scannedValue.isBrowsableUrl())
    }

    @Test
    fun `gallery qr code containing plain text is not browsable`() {
        val scannedValue = "Scanora V2"

        assertFalse(scannedValue.isBrowsableUrl())
    }

    @Test
    fun `gallery qr code containing email address is not browsable`() {
        val scannedValue = "hello@example.com"

        assertFalse(scannedValue.isBrowsableUrl())
    }

    @Test
    fun `gallery qr code containing wifi configuration is not browsable`() {
        val scannedValue = "WIFI:T:WPA;S:MyNetwork;P:password123;;"

        assertFalse(scannedValue.isBrowsableUrl())
    }

    @Test
    fun `gallery qr code containing contact information is not browsable`() {
        val scannedValue = "BEGIN:VCARD\nFN:John Doe\nEND:VCARD"

        assertFalse(scannedValue.isBrowsableUrl())
    }
}