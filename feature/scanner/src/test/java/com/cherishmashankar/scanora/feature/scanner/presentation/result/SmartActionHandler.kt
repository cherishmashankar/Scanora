
package com.cherishmashankar.scanora.feature.scanner.presentation.result

import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.provider.Settings
import com.cherishmashankar.scanora.core.model.ScanContent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
internal class SmartActionHandlerTest {

    private lateinit var activity: Activity
    private lateinit var handler: SmartActionHandler

    @Before
    fun setUp() {
        activity = Robolectric.buildActivity(Activity::class.java)
            .setup()
            .get()

        handler = SmartActionHandler(activity)
    }

    @Test
    fun `copy action copies raw scanned value`() {
        val rawValue = "Hello Scanora"

        val result = handler.execute(
            action = SmartAction.COPY,
            content = ScanContent.Text(rawValue),
            rawValue = rawValue
        )

        assertTrue(result.exceptionOrNull()?.toString(), result.isSuccess)

        val clipboard = activity.getSystemService(
            Context.CLIPBOARD_SERVICE
        ) as ClipboardManager

        assertEquals(
            rawValue,
            clipboard.primaryClip
                ?.getItemAt(0)
                ?.coerceToText(activity)
                ?.toString()
        )
    }

    @Test
    fun `open url action launches browser intent`() {
        val url = "https://example.com"

        val result = handler.execute(
            action = SmartAction.OPEN_URL,
            content = ScanContent.Url(url),
            rawValue = url
        )

        assertTrue(result.exceptionOrNull()?.toString(), result.isSuccess)

        val intent = shadowOf(activity).nextStartedActivity

        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals(url, intent.dataString)
    }

    @Test
    fun `dial phone action launches dialer`() {
        val number = "+49123456789"

        val result = handler.execute(
            action = SmartAction.DIAL_PHONE,
            content = ScanContent.Phone(number),
            rawValue = number
        )

        assertTrue(result.exceptionOrNull()?.toString(), result.isSuccess)

        val intent = shadowOf(activity).nextStartedActivity

        assertEquals(Intent.ACTION_DIAL, intent.action)
        assertEquals("tel:$number", intent.dataString)
    }

    @Test
    fun `wifi action opens wifi settings`() {
        val result = handler.execute(
            action = SmartAction.CONNECT_WIFI,
            content = ScanContent.Wifi(
                ssid = "HomeNetwork",
                password = "password123",
                securityType = "WPA"
            ),
            rawValue = "WIFI:T:WPA;S:HomeNetwork;P:password123;;"
        )

        assertTrue(result.exceptionOrNull()?.toString(), result.isSuccess)

        val intent = shadowOf(activity).nextStartedActivity

        assertEquals(
            Settings.ACTION_WIFI_SETTINGS,
            intent.action
        )
    }

    @Test
    fun `invalid action for content returns failure`() {
        val result = handler.execute(
            action = SmartAction.DIAL_PHONE,
            content = ScanContent.Text("Hello Scanora"),
            rawValue = "Hello Scanora"
        )

        assertTrue(result.isFailure)
    }

    @Test
    fun `send email action launches email intent`() {
        val content = ScanContent.Email(
            address = "hello@example.com",
            subject = "Scanora Test",
            body = "Hello from Scanora"
        )

        val result = handler.execute(
            action = SmartAction.SEND_EMAIL,
            content = content,
            rawValue = "mailto:hello@example.com"
        )

        assertTrue(result.exceptionOrNull()?.toString(), result.isSuccess)

        val intent = shadowOf(activity).nextStartedActivity

        assertEquals(Intent.ACTION_SENDTO, intent.action)
        assertEquals("mailto:", intent.dataString)
        assertEquals(
            "hello@example.com",
            intent.getStringArrayExtra(Intent.EXTRA_EMAIL)?.firstOrNull()
        )
        assertEquals(
            "Scanora Test",
            intent.getStringExtra(Intent.EXTRA_SUBJECT)
        )
        assertEquals(
            "Hello from Scanora",
            intent.getStringExtra(Intent.EXTRA_TEXT)
        )
    }

    @Test
    fun `add contact action launches contact insertion intent`() {
        val content = ScanContent.Contact(
            name = "Alex Smith",
            phone = "+49123456789",
            email = "alex@example.com"
        )

        val result = handler.execute(
            action = SmartAction.ADD_CONTACT,
            content = content,
            rawValue = "BEGIN:VCARD..."
        )

        assertTrue(result.exceptionOrNull()?.toString(), result.isSuccess)

        val intent = shadowOf(activity).nextStartedActivity

        assertEquals(Intent.ACTION_INSERT, intent.action)
        assertEquals(
            ContactsContract.Contacts.CONTENT_URI,
            intent.data
        )
        assertEquals(
            "Alex Smith",
            intent.getStringExtra(ContactsContract.Intents.Insert.NAME)
        )
        assertEquals(
            "+49123456789",
            intent.getStringExtra(ContactsContract.Intents.Insert.PHONE)
        )
        assertEquals(
            "alex@example.com",
            intent.getStringExtra(ContactsContract.Intents.Insert.EMAIL)
        )
    }

    @Test
    fun `share action launches chooser with scanned value`() {
        val rawValue = "Hello Scanora"

        val result = handler.execute(
            action = SmartAction.SHARE,
            content = ScanContent.Text(rawValue),
            rawValue = rawValue
        )

        assertTrue(result.exceptionOrNull()?.toString(), result.isSuccess)

        val chooserIntent = shadowOf(activity).nextStartedActivity

        assertEquals(Intent.ACTION_CHOOSER, chooserIntent.action)

        @Suppress("DEPRECATION")
        val shareIntent = chooserIntent.getParcelableExtra<Intent>(
            Intent.EXTRA_INTENT
        )

        assertEquals(Intent.ACTION_SEND, shareIntent?.action)
        assertEquals("text/plain", shareIntent?.type)
        assertEquals(
            rawValue,
            shareIntent?.getStringExtra(Intent.EXTRA_TEXT)
        )
    }

}
