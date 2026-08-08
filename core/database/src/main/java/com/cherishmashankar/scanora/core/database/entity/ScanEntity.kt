package com.cherishmashankar.scanora.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "scans"
)
data class ScanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val value: String,

    val format: String,

    val timestamp: Long,

    val isFavorite: Boolean = false
)