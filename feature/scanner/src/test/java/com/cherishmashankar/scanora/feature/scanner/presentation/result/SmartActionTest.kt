package com.cherishmashankar.scanora.feature.scanner.presentation.result

import com.cherishmashankar.scanora.core.model.ScanContent
import org.junit.Assert.assertEquals
import org.junit.Test

class SmartActionTest {

    @Test
    fun `url provides open copy and share`() {
        val content = ScanContent.Url("https://example.com")

        assertEquals(
            listOf(
                SmartAction.OPEN_URL,
                SmartAction.COPY,
                SmartAction.SHARE
            ),
            content.availableActions()
        )
    }

    @Test
    fun `wifi provides connect copy and share`() {
        val content = ScanContent.Wifi(
            ssid = "HomeNetwork",
            password = "password123",
            securityType = "WPA"
        )

        assertEquals(
            listOf(
                SmartAction.CONNECT_WIFI,
                SmartAction.COPY,
                SmartAction.SHARE
            ),
            content.availableActions()
        )
    }

    @Test
    fun `email provides send copy and share`() {
        val content = ScanContent.Email(
            address = "hello@example.com",
            subject = "Hello",
            body = "Test message"
        )

        assertEquals(
            listOf(
                SmartAction.SEND_EMAIL,
                SmartAction.COPY,
                SmartAction.SHARE
            ),
            content.availableActions()
        )
    }

    @Test
    fun `phone provides dial copy and share`() {
        val content = ScanContent.Phone("+49123456789")

        assertEquals(
            listOf(
                SmartAction.DIAL_PHONE,
                SmartAction.COPY,
                SmartAction.SHARE
            ),
            content.availableActions()
        )
    }

    @Test
    fun `contact provides add contact copy and share`() {
        val content = ScanContent.Contact(
            name = "Alex Smith",
            phone = "+49123456789",
            email = "alex@example.com"
        )

        assertEquals(
            listOf(
                SmartAction.ADD_CONTACT,
                SmartAction.COPY,
                SmartAction.SHARE
            ),
            content.availableActions()
        )
    }

    @Test
    fun `plain text provides copy and share only`() {
        val content = ScanContent.Text("Hello Scanora")

        assertEquals(
            listOf(
                SmartAction.COPY,
                SmartAction.SHARE
            ),
            content.availableActions()
        )
    }
}
