package com.cherishmashankar.scanora.feature.scanner.presentation.result

internal fun String.toBrowsableUrl(): String {
    val value = trim()

    return when {
        value.startsWith("https://", ignoreCase = true) -> value
        value.startsWith("http://", ignoreCase = true) -> value
        value.startsWith("www.", ignoreCase = true) -> "https://$value"
        else -> value
    }
}

internal fun String.isBrowsableUrl(): Boolean {
    val value = trim()

    return value.startsWith("https://", ignoreCase = true) ||
            value.startsWith("http://", ignoreCase = true) ||
            value.startsWith("www.", ignoreCase = true)
}