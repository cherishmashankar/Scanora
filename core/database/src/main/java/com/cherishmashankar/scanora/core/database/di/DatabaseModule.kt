package com.cherishmashankar.scanora.core.database.di

import android.content.Context
import androidx.room.Room
import com.cherishmashankar.scanora.core.database.ScanoraDatabase
import com.cherishmashankar.scanora.core.database.dao.ScanDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideScanoraDatabase(
        @ApplicationContext context: Context
    ): ScanoraDatabase {
        return Room.databaseBuilder(
            context,
            ScanoraDatabase::class.java,
            "scanora.db"
        ).build()
    }

    @Provides
    fun provideScanDao(
        database: ScanoraDatabase
    ): ScanDao {
        return database.scanDao()
    }
}