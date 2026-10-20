
package com.cherishmashankar.scanora.core.domain

import com.cherishmashankar.scanora.core.model.ScanContent
import org.junit.Assert.assertEquals
import org.junit.Test

class ScanContentDetectorTest {

    private val detector = ScanContentDetector()

    // --------------------------------------------------
    // URL
    // --------------------------------------------------

    @Test
    fun `detects HTTPS URL`() {
        assertEquals(
            ScanContent.Url("https://example.com"),
            detector.detect("https://example.com")
        )
    }

    @Test
    fun `detects HTTP URL`() {
        assertEquals(
            ScanContent.Url("http://example.com"),
            detector.detect("http://example.com")
        )
    }

    @Test
    fun `adds HTTPS to www URL`() {
        assertEquals(
            ScanContent.Url("https://www.example.com"),
            detector.detect("www.example.com")
        )
    }

    @Test
    fun `detects URL case insensitively`() {
        assertEquals(
            ScanContent.Url("HTTPS://EXAMPLE.COM"),
            detector.detect("HTTPS://EXAMPLE.COM")
        )
    }

    @Test
    fun `trims URL whitespace`() {
        assertEquals(
            ScanContent.Url("https://example.com"),
            detector.detect("  https://example.com  ")
        )
    }

    @Test
    fun `rejects URL without hostname`() {
        val value = "https://"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `rejects URL containing spaces`() {
        val value = "https://example.com/hello world"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    // --------------------------------------------------
    // WI-FI
    // --------------------------------------------------

    @Test
    fun `detects WPA WiFi network`() {
        assertEquals(
            ScanContent.Wifi(
                ssid = "HomeNetwork",
                password = "password123",
                securityType = "WPA"
            ),
            detector.detect(
                "WIFI:T:WPA;S:HomeNetwork;P:password123;;"
            )
        )
    }

    @Test
    fun `detects WEP WiFi network`() {
        assertEquals(
            ScanContent.Wifi(
                ssid = "LegacyNetwork",
                password = "abc12345",
                securityType = "WEP"
            ),
            detector.detect(
                "WIFI:T:WEP;S:LegacyNetwork;P:abc12345;;"
            )
        )
    }

    @Test
    fun `detects open WiFi network`() {
        assertEquals(
            ScanContent.Wifi(
                ssid = "GuestNetwork",
                password = null,
                securityType = "NOPASS"
            ),
            detector.detect(
                "WIFI:T:nopass;S:GuestNetwork;;"
            )
        )
    }

    @Test
    fun `defaults missing WiFi security type to NOPASS`() {
        assertEquals(
            ScanContent.Wifi(
                ssid = "CafeNetwork",
                password = null,
                securityType = "NOPASS"
            ),
            detector.detect(
                "WIFI:S:CafeNetwork;;"
            )
        )
    }

    @Test
    fun `handles escaped WiFi characters`() {
        assertEquals(
            ScanContent.Wifi(
                ssid = "My;Network",
                password = "abc:123",
                securityType = "WPA"
            ),
            detector.detect(
                """WIFI:T:WPA;S:My\;Network;P:abc\:123;;"""
            )
        )
    }

    @Test
    fun `handles WiFi fields in different order`() {
        assertEquals(
            ScanContent.Wifi(
                ssid = "Home",
                password = "secret123",
                securityType = "WPA"
            ),
            detector.detect(
                "WIFI:P:secret123;S:Home;T:WPA;;"
            )
        )
    }

    @Test
    fun `rejects WiFi without SSID`() {
        val value = "WIFI:T:WPA;P:password123;;"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `rejects WiFi with empty SSID`() {
        val value = "WIFI:T:WPA;S:;P:password123;;"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `rejects secured WiFi without password`() {
        val value = "WIFI:T:WPA;S:HomeNetwork;;"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `rejects unsupported WiFi security type`() {
        val value = "WIFI:T:UNKNOWN;S:Home;P:secret;;"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `rejects unfinished WiFi escape`() {
        val value = "WIFI:T:WPA;S:Home;P:secret\\"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    // --------------------------------------------------
    // EMAIL
    // --------------------------------------------------

    @Test
    fun `detects plain email address`() {
        assertEquals(
            ScanContent.Email(address = "hello@example.com"),
            detector.detect("hello@example.com")
        )
    }

    @Test
    fun `detects mailto email`() {
        assertEquals(
            ScanContent.Email(address = "hello@example.com"),
            detector.detect("mailto:hello@example.com")
        )
    }

    @Test
    fun `detects mailto with subject and body`() {
        assertEquals(
            ScanContent.Email(
                address = "hello@example.com",
                subject = "Hello World",
                body = "How are you?"
            ),
            detector.detect(
                "mailto:hello@example.com" +
                        "?subject=Hello%20World" +
                        "&body=How%20are%20you%3F"
            )
        )
    }

    @Test
    fun `detects mailto case insensitively`() {
        assertEquals(
            ScanContent.Email(address = "hello@example.com"),
            detector.detect("MAILTO:hello@example.com")
        )
    }

    @Test
    fun `rejects email without domain`() {
        val value = "hello@"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `rejects malformed mailto`() {
        val value = "mailto:not-an-email"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    // --------------------------------------------------
    // PHONE
    // --------------------------------------------------

    @Test
    fun `detects international phone number`() {
        assertEquals(
            ScanContent.Phone("+491701234567"),
            detector.detect("+491701234567")
        )
    }

    @Test
    fun `detects tel URI`() {
        assertEquals(
            ScanContent.Phone("+491701234567"),
            detector.detect("tel:+491701234567")
        )
    }

    @Test
    fun `detects formatted phone number`() {
        assertEquals(
            ScanContent.Phone("+49 170 1234567"),
            detector.detect("+49 170 1234567")
        )
    }

    @Test
    fun `detects phone number with parentheses`() {
        assertEquals(
            ScanContent.Phone("(030) 12345678"),
            detector.detect("(030) 12345678")
        )
    }

    @Test
    fun `rejects short phone number`() {
        val value = "12345"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `rejects phone number containing letters`() {
        val value = "tel:ABC123456"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    // --------------------------------------------------
    // CONTACT - VCARD
    // --------------------------------------------------

    @Test
    fun `detects vCard contact`() {
        val value = """
            BEGIN:VCARD
            VERSION:3.0
            FN:John Doe
            TEL:+491701234567
            EMAIL:john@example.com
            END:VCARD
        """.trimIndent()

        assertEquals(
            ScanContent.Contact(
                name = "John Doe",
                phone = "+491701234567",
                email = "john@example.com"
            ),
            detector.detect(value)
        )
    }

    @Test
    fun `detects vCard with typed fields`() {
        val value = """
            BEGIN:VCARD
            VERSION:3.0
            FN:Jane Doe
            TEL;TYPE=CELL:+491701112233
            EMAIL;TYPE=WORK:jane@example.com
            END:VCARD
        """.trimIndent()

        assertEquals(
            ScanContent.Contact(
                name = "Jane Doe",
                phone = "+491701112233",
                email = "jane@example.com"
            ),
            detector.detect(value)
        )
    }

    @Test
    fun `detects vCard name from N field`() {
        val value = """
            BEGIN:VCARD
            VERSION:3.0
            N:Doe;John;;;
            TEL:+491701234567
            END:VCARD
        """.trimIndent()

        assertEquals(
            ScanContent.Contact(
                name = "John Doe",
                phone = "+491701234567",
                email = null
            ),
            detector.detect(value)
        )
    }

    @Test
    fun `detects vCard with Windows line endings`() {
        val value = listOf(
            "BEGIN:VCARD",
            "VERSION:3.0",
            "FN:John Doe",
            "TEL:+491701234567",
            "END:VCARD"
        ).joinToString("\r\n")

        assertEquals(
            ScanContent.Contact(
                name = "John Doe",
                phone = "+491701234567",
                email = null
            ),
            detector.detect(value)
        )
    }

    @Test
    fun `rejects vCard without END marker`() {
        val value = """
            BEGIN:VCARD
            FN:John Doe
            TEL:+491701234567
        """.trimIndent()

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `rejects empty vCard`() {
        val value = """
            BEGIN:VCARD
            VERSION:3.0
            END:VCARD
        """.trimIndent()

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    // --------------------------------------------------
    // CONTACT - MECARD
    // --------------------------------------------------

    @Test
    fun `detects MECARD contact`() {
        assertEquals(
            ScanContent.Contact(
                name = "John Doe",
                phone = "+491701234567",
                email = "john@example.com"
            ),
            detector.detect(
                "MECARD:N:Doe,John;" +
                        "TEL:+491701234567;" +
                        "EMAIL:john@example.com;;"
            )
        )
    }

    @Test
    fun `detects MECARD with phone only`() {
        assertEquals(
            ScanContent.Contact(
                name = null,
                phone = "+491701234567",
                email = null
            ),
            detector.detect(
                "MECARD:TEL:+491701234567;;"
            )
        )
    }

    @Test
    fun `rejects empty MECARD`() {
        val value = "MECARD:;;"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    // --------------------------------------------------
    // PLAIN TEXT AND FALLBACK
    // --------------------------------------------------

    @Test
    fun `detects ordinary text`() {
        assertEquals(
            ScanContent.Text("Hello Scanora"),
            detector.detect("Hello Scanora")
        )
    }

    @Test
    fun `preserves whitespace in plain text`() {
        val value = "  Hello Scanora  "

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `detects empty string as text`() {
        assertEquals(
            ScanContent.Text(""),
            detector.detect("")
        )
    }

    @Test
    fun `detects whitespace only as text`() {
        val value = "   "

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `keeps barcode numbers as plain text`() {
        val value = "1234567890123"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `keeps arbitrary numeric text as plain text`() {
        val value = "123456789"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }



    @Test
    fun `rejects duplicate WiFi SSID fields`() {
        val value = "WIFI:T:WPA;S:Home;S:Other;P:secret123;;"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `rejects WiFi with invalid escape sequence`() {
        val value = """WIFI:T:WPA;S:Home\q;P:secret123;;"""

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `preserves escaped WiFi backslash`() {
        assertEquals(
            ScanContent.Wifi(
                ssid = """Home\Network""",
                password = "secret123",
                securityType = "WPA"
            ),
            detector.detect(
                """WIFI:T:WPA;S:Home\\Network;P:secret123;;"""
            )
        )
    }

    @Test
    fun `preserves plus sign in mailto address`() {
        assertEquals(
            ScanContent.Email(
                address = "john+work@example.com",
                subject = "Hello World"
            ),
            detector.detect(
                "mailto:john+work@example.com?subject=Hello%20World"
            )
        )
    }

    @Test
    fun `rejects URL with embedded credentials`() {
        val value = "https://user:password@example.com"

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `rejects URL with backslash`() {
        val value = """https://example.com\@other.com"""

        assertEquals(
            ScanContent.Text(value),
            detector.detect(value)
        )
    }

    @Test
    fun `handles folded vCard lines`() {
        val value = """
        BEGIN:VCARD
        VERSION:3.0
        FN:John
         Doe
        TEL:+491701234567
        END:VCARD
    """.trimIndent()

        assertEquals(
            ScanContent.Contact(
                name = "JohnDoe",
                phone = "+491701234567",
                email = null
            ),
            detector.detect(value)
        )
    }

    @Test
    fun `unescapes vCard contact name`() {
        val value = """
        BEGIN:VCARD
        VERSION:3.0
        FN:Doe\, John
        TEL:+491701234567
        END:VCARD
    """.trimIndent()

        assertEquals(
            ScanContent.Contact(
                name = "Doe, John",
                phone = "+491701234567",
                email = null
            ),
            detector.detect(value)
        )
    }

    @Test
    fun `does not classify numeric barcode as phone`() {
        assertEquals(
            ScanContent.Text("1234567890123"),
            detector.detect("1234567890123")
        )
    }

}

