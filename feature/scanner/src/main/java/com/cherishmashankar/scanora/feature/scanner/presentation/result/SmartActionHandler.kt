package com.cherishmashankar.scanora.feature.scanner.presentation.result

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.provider.Settings
import androidx.core.net.toUri
import com.cherishmashankar.scanora.core.model.ScanContent

internal class SmartActionHandler(
    private val context: Context
) {

    fun execute(
        action: SmartAction,
        content: ScanContent,
        rawValue: String
    ): Result<Unit> = runCatching {
        require(action in content.availableActions()) {
            "Action $action is not available for this content type"
        }

        when (action) {
            SmartAction.OPEN_URL -> {
                val url = (content as ScanContent.Url).url

                val intent = Intent(
                    Intent.ACTION_VIEW,
                    url.toUri()
                )

                context.startActivity(intent)
            }

            SmartAction.CONNECT_WIFI -> {
                context.startActivity(
                    Intent(Settings.ACTION_WIFI_SETTINGS)
                )
            }

            SmartAction.SEND_EMAIL -> {
                val email = content as ScanContent.Email

                val intent = Intent(
                    Intent.ACTION_SENDTO,
                    "mailto:".toUri()
                ).apply {
                    putExtra(
                        Intent.EXTRA_EMAIL,
                        arrayOf(email.address)
                    )

                    email.subject?.let {
                        putExtra(Intent.EXTRA_SUBJECT, it)
                    }

                    email.body?.let {
                        putExtra(Intent.EXTRA_TEXT, it)
                    }
                }

                context.startActivity(intent)
            }

            SmartAction.DIAL_PHONE -> {
                val phone = content as ScanContent.Phone

                val intent = Intent(
                    Intent.ACTION_DIAL,
                    "tel:${phone.number}".toUri()
                )

                context.startActivity(intent)
            }

            SmartAction.ADD_CONTACT -> {
                val contact = content as ScanContent.Contact

                val intent = Intent(
                    Intent.ACTION_INSERT,
                    ContactsContract.Contacts.CONTENT_URI
                ).apply {
                    contact.name?.let {
                        putExtra(
                            ContactsContract.Intents.Insert.NAME,
                            it
                        )
                    }

                    contact.phone?.let {
                        putExtra(
                            ContactsContract.Intents.Insert.PHONE,
                            it
                        )
                    }

                    contact.email?.let {
                        putExtra(
                            ContactsContract.Intents.Insert.EMAIL,
                            it
                        )
                    }
                }

                context.startActivity(intent)
            }

            SmartAction.COPY -> {
                val clipboard = context.getSystemService(
                    Context.CLIPBOARD_SERVICE
                ) as ClipboardManager

                clipboard.setPrimaryClip(
                    ClipData.newPlainText(
                        "Scanned content",
                        rawValue
                    )
                )
            }

            SmartAction.SHARE -> {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, rawValue)
                }

                context.startActivity(
                    Intent.createChooser(intent, null)
                )
            }
        }
    }
}
