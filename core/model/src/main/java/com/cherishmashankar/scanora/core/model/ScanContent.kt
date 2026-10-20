package com.cherishmashankar.scanora.core.model

sealed interface ScanContent {

    data class Url(
        val url: String
    ) : ScanContent

    data class Wifi(
        val ssid: String,
        val password: String?,
        val securityType: String
    ) : ScanContent

    data class Email(
        val address: String,
        val subject: String? = null,
        val body: String? = null
    ) : ScanContent

    data class Phone(
        val number: String
    ) : ScanContent

    data class Contact(
        val name: String?,
        val phone: String?,
        val email: String?
    ) : ScanContent

    data class Text(
        val value: String
    ) : ScanContent
}