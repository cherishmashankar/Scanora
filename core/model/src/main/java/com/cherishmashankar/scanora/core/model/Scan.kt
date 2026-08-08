package com.cherishmashankar.scanora.core.model

data class Scan(
    val id: Long = 0,
    val value: String,
    val format: ScanFormat,
    val timestamp: Long,
    val isFavorite: Boolean = false
)