package com.cherishmashankar.scanora.feature.scanner.presentation.result

import com.cherishmashankar.scanora.core.model.ScanContent

enum class SmartAction {
    OPEN_URL,
    CONNECT_WIFI,
    SEND_EMAIL,
    DIAL_PHONE,
    ADD_CONTACT,
    COPY,
    SHARE
}

fun ScanContent.availableActions(): List<SmartAction> =
    when (this) {
        is ScanContent.Url -> listOf(
            SmartAction.OPEN_URL,
            SmartAction.COPY,
            SmartAction.SHARE
        )

        is ScanContent.Wifi -> listOf(
            SmartAction.CONNECT_WIFI,
            SmartAction.COPY,
            SmartAction.SHARE
        )

        is ScanContent.Email -> listOf(
            SmartAction.SEND_EMAIL,
            SmartAction.COPY,
            SmartAction.SHARE
        )

        is ScanContent.Phone -> listOf(
            SmartAction.DIAL_PHONE,
            SmartAction.COPY,
            SmartAction.SHARE
        )

        is ScanContent.Contact -> listOf(
            SmartAction.ADD_CONTACT,
            SmartAction.COPY,
            SmartAction.SHARE
        )

        is ScanContent.Text -> listOf(
            SmartAction.COPY,
            SmartAction.SHARE
        )
    }