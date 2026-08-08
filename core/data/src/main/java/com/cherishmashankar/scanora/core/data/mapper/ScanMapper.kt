package com.cherishmashankar.scanora.core.data.mapper

import com.cherishmashankar.scanora.core.database.entity.ScanEntity
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.core.model.ScanFormat

internal fun ScanEntity.toModel(): Scan =
    Scan(
        id = id,
        value = value,
        format = ScanFormat.valueOf(format),
        timestamp = timestamp,
        isFavorite = isFavorite
    )

internal fun Scan.toEntity(): ScanEntity =
    ScanEntity(
        id = id,
        value = value,
        format = format.name,
        timestamp = timestamp,
        isFavorite = isFavorite
    )

