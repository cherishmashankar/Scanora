package com.cherishmashankar.scanora.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cherishmashankar.scanora.core.database.dao.ScanDao
import com.cherishmashankar.scanora.core.database.entity.ScanEntity

@Database(
    entities = [
        ScanEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ScanoraDatabase : RoomDatabase() {

    abstract fun scanDao(): ScanDao
}