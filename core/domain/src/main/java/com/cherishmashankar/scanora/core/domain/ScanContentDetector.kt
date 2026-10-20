
package com.cherishmashankar.scanora.core.domain

import com.cherishmashankar.scanora.core.model.ScanContent
import jakarta.inject.Inject
import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.Locale

class ScanContentDetector @Inject constructor() {

    fun detect(value: String): ScanContent {
        val trimmed = value.trim()

        detectWifi(trimmed)?.let { return it }
        detectContact(trimmed)?.let { return it }
        detectEmail(trimmed)?.let { return it }
        detectPhone(trimmed)?.let { return it }
        detectUrl(trimmed)?.let { return it }

        return ScanContent.Text(value)
    }

    // --------------------------------------------------
    // URL
    // --------------------------------------------------


    private fun detectUrl(value: String): ScanContent.Url? {
        val url = when {
            value.startsWith("https://", ignoreCase = true) ||
                    value.startsWith("http://", ignoreCase = true) -> value

            value.startsWith("www.", ignoreCase = true) ->
                "https://$value"

            else -> return null
        }

        if (url.any { it.isISOControl() || it.isWhitespace() || it == '\\' }) {
            return null
        }

        return try {
            val uri = URI(url)
            val scheme = uri.scheme?.lowercase(Locale.ROOT)
            val host = uri.host

            if (
                scheme !in setOf("http", "https") ||
                uri.isOpaque ||
                uri.rawUserInfo != null ||
                host.isNullOrBlank() ||
                host.any { it.isISOControl() || it.isWhitespace() } ||
                uri.port > 65535 ||
                uri.port < -1
            ) {
                null
            } else {
                ScanContent.Url(url)
            }
        } catch (_: java.net.URISyntaxException) {
            null
        } catch (_: IllegalArgumentException) {
            null
        }
    }


    // --------------------------------------------------
    // WI-FI
    // --------------------------------------------------

    private fun detectWifi(value: String): ScanContent.Wifi? {
        if (!value.startsWith("WIFI:", ignoreCase = true)) {
            return null
        }

        val fields = parseDelimitedFields(
            input = value.substring(5),
            allowedKeys = setOf("T", "S", "P", "H")
        ) ?: return null

        val ssid = fields["S"]
            ?.takeIf { it.isNotBlank() }
            ?: return null

        val security = fields["T"]
            ?.uppercase(Locale.ROOT)
            ?.ifBlank { "NOPASS" }
            ?: "NOPASS"

        if (security !in setOf("WPA", "WEP", "NOPASS")) {
            return null
        }

        val password = fields["P"]

        if (security != "NOPASS" && password.isNullOrEmpty()) {
            return null
        }

        if (
            fields["H"] != null &&
            fields["H"] !in setOf("true", "false", "TRUE", "FALSE")
        ) {
            return null
        }

        return ScanContent.Wifi(
            ssid = ssid,
            password = if (security == "NOPASS") null else password,
            securityType = security
        )
    }

    // --------------------------------------------------
    // EMAIL
    // --------------------------------------------------

    private fun detectEmail(value: String): ScanContent.Email? {
        if (value.startsWith("mailto:", ignoreCase = true)) {
            return detectMailto(value)
        }

        return if (EMAIL_REGEX.matches(value)) {
            ScanContent.Email(address = value)
        } else {
            null
        }
    }

    private fun detectMailto(value: String): ScanContent.Email? {
        return try {
            val uri = URI(value)

            if (!uri.scheme.equals("mailto", ignoreCase = true)) {
                return null
            }

            val rawContent = uri.rawSchemeSpecificPart
            val rawAddress = rawContent.substringBefore("?")

            // Preserve literal '+' in addresses such as john+work@example.com.
            val address = decodePercentEncoded(rawAddress)

            if (!EMAIL_REGEX.matches(address)) {
                return null
            }

            val query = rawContent.substringAfter(
                "?",
                missingDelimiterValue = ""
            )

            val parameters = mutableMapOf<String, String>()

            if (query.isNotEmpty()) {
                for (parameter in query.split("&")) {
                    val separator = parameter.indexOf('=')
                    if (separator < 0) continue

                    val key = decodeFormEncoded(
                        parameter.substring(0, separator)
                    ).lowercase(Locale.ROOT)

                    val content = decodeFormEncoded(
                        parameter.substring(separator + 1)
                    )

                    // Do not silently overwrite repeated parameters.
                    if (key in parameters) return null

                    parameters[key] = content
                }
            }

            ScanContent.Email(
                address = address,
                subject = parameters["subject"],
                body = parameters["body"]
            )
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    // --------------------------------------------------
    // PHONE
    // --------------------------------------------------

    private fun detectPhone(value: String): ScanContent.Phone? {
        val hasTelPrefix = value.startsWith(
            "tel:",
            ignoreCase = true
        )

        val number = if (hasTelPrefix) {
            value.substring(4).trim()
        } else {
            value
        }

        val normalized = number.replace(
            Regex("[\\s().-]"),
            ""
        )

        if (!PHONE_REGEX.matches(normalized)) {
            return null
        }

        // Bare numbers could be barcodes, IDs or phone numbers.
        val hasPhoneIndicator =
            number.startsWith("+") ||
                    number.any {
                        it == ' ' || it == '(' ||
                                it == ')' || it == '-'
                    }

        if (!hasTelPrefix && !hasPhoneIndicator) {
            return null
        }

        return ScanContent.Phone(number)
    }

    // --------------------------------------------------
    // CONTACT
    // --------------------------------------------------

    private fun detectContact(value: String): ScanContent.Contact? {
        return when {
            value.startsWith("BEGIN:VCARD", ignoreCase = true) ->
                detectVCard(value)

            value.startsWith("MECARD:", ignoreCase = true) ->
                detectMeCard(value)

            else -> null
        }
    }

    private fun detectVCard(value: String): ScanContent.Contact? {
        val lines = unfoldVCardLines(value)

        if (
            !lines.firstOrNull()
                .equals("BEGIN:VCARD", ignoreCase = true) ||
            !lines.lastOrNull()
                .equals("END:VCARD", ignoreCase = true)
        ) {
            return null
        }

        val fields = mutableMapOf<String, String>()

        for (line in lines.drop(1).dropLast(1)) {
            val separator = line.indexOf(':')
            if (separator <= 0) continue

            val property = line.substring(0, separator)
                .substringBefore(';')
                .substringAfterLast('.')
                .uppercase(Locale.ROOT)

            if (property !in setOf("FN", "N", "TEL", "EMAIL")) {
                continue
            }

            // Keep the first value for each supported property.
            if (property !in fields) {
                fields[property] = unescapeVCard(
                    line.substring(separator + 1)
                ) ?: return null
            }
        }

        val name = fields["FN"] ?: fields["N"]?.let {
            val parts = it.split(';')
            listOfNotNull(
                parts.getOrNull(1)?.takeIf(String::isNotBlank),
                parts.getOrNull(0)?.takeIf(String::isNotBlank)
            ).joinToString(" ")
        }

        return createContact(
            name = name,
            phone = fields["TEL"],
            email = fields["EMAIL"]
        )
    }

    private fun detectMeCard(value: String): ScanContent.Contact? {
        val fields = parseDelimitedFields(
            input = value.substring(7),
            allowedKeys = setOf(
                "N", "TEL", "EMAIL", "ADR",
                "URL", "ORG", "NOTE", "BDAY",
                "SOUND", "NICKNAME"
            )
        ) ?: return null

        val name = fields["N"]
            ?.split(',')
            ?.let { parts ->
                listOfNotNull(
                    parts.getOrNull(1)?.takeIf(String::isNotBlank),
                    parts.getOrNull(0)?.takeIf(String::isNotBlank)
                ).joinToString(" ")
            }

        return createContact(
            name = name,
            phone = fields["TEL"],
            email = fields["EMAIL"]
        )
    }

    private fun createContact(
        name: String?,
        phone: String?,
        email: String?
    ): ScanContent.Contact? {
        val cleanName = name?.trim()?.takeIf(String::isNotEmpty)
        val cleanPhone = phone?.trim()?.takeIf(String::isNotEmpty)
        val cleanEmail = email?.trim()?.takeIf(String::isNotEmpty)

        if (
            cleanName == null &&
            cleanPhone == null &&
            cleanEmail == null
        ) {
            return null
        }

        return ScanContent.Contact(
            name = cleanName,
            phone = cleanPhone,
            email = cleanEmail
        )
    }

    // --------------------------------------------------
    // SHARED PARSING HELPERS
    // --------------------------------------------------

    private fun parseDelimitedFields(
        input: String,
        allowedKeys: Set<String>
    ): Map<String, String>? {
        val fields = mutableMapOf<String, String>()
        val parts = mutableListOf<String>()
        val current = StringBuilder()
        var escaped = false

        // Split on unescaped semicolons, preserving escapes.
        for (character in input) {
            when {
                escaped -> {
                    current.append('\\')
                    current.append(character)
                    escaped = false
                }

                character == '\\' -> escaped = true

                character == ';' -> {
                    parts.add(current.toString())
                    current.clear()
                }

                else -> current.append(character)
            }
        }

        if (escaped) return null

        if (current.isNotEmpty()) {
            parts.add(current.toString())
        }

        for (part in parts) {
            if (part.isEmpty()) continue

            val separator = findUnescapedColon(part)
            if (separator <= 0) return null

            val key = part.substring(0, separator)
                .uppercase(Locale.ROOT)

            if (key !in allowedKeys || key in fields) {
                return null
            }

            val rawValue = part.substring(separator + 1)
            val decodedValue = unescapeDelimited(rawValue)
                ?: return null

            fields[key] = decodedValue
        }

        return fields
    }

    private fun findUnescapedColon(value: String): Int {
        var escaped = false

        for (index in value.indices) {
            val character = value[index]

            when {
                escaped -> escaped = false
                character == '\\' -> escaped = true
                character == ':' -> return index
            }
        }

        return -1
    }

    private fun unescapeDelimited(value: String): String? {
        val result = StringBuilder()
        var escaped = false

        for (character in value) {
            when {
                escaped -> {
                    if (character !in setOf(
                            '\\', ';', ':', ',', '"'
                        )
                    ) {
                        return null
                    }

                    result.append(character)
                    escaped = false
                }

                character == '\\' -> escaped = true

                else -> result.append(character)
            }
        }

        return if (escaped) null else result.toString()
    }

    private fun unfoldVCardLines(value: String): List<String> {
        val rawLines = value
            .replace("\r\n", "\n")
            .replace('\r', '\n')
            .lines()

        val result = mutableListOf<String>()

        for (line in rawLines) {
            if (
                line.startsWith(" ") ||
                line.startsWith("\t")
            ) {
                if (result.isEmpty()) return emptyList()

                val lastIndex = result.lastIndex
                result[lastIndex] += line.substring(1)
            } else {
                result.add(line)
            }
        }

        return result.dropLastWhile { it.isBlank() }
    }

    private fun unescapeVCard(value: String): String? {
        val result = StringBuilder()
        var index = 0

        while (index < value.length) {
            val character = value[index]

            if (character != '\\') {
                result.append(character)
                index++
                continue
            }

            if (index + 1 >= value.length) return null

            val next = value[index + 1]

            result.append(
                when (next) {
                    'n', 'N' -> '\n'
                    '\\' -> '\\'
                    ',' -> ','
                    ';' -> ';'
                    else -> return null
                }
            )

            index += 2
        }

        return result.toString()
    }

    private fun decodePercentEncoded(value: String): String {
        return URLDecoder.decode(
            value.replace("+", "%2B"),
            StandardCharsets.UTF_8.name()
        )
    }

    private fun decodeFormEncoded(value: String): String {
        return URLDecoder.decode(
            value,
            StandardCharsets.UTF_8.name()
        )
    }

    companion object {
        private val EMAIL_REGEX = Regex(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )

        private val PHONE_REGEX = Regex(
            "^\\+?[0-9]{7,15}$"
        )
    }
}
