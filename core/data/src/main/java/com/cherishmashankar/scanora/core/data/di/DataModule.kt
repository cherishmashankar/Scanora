package com.cherishmashankar.scanora.core.data.di

import com.cherishmashankar.scanora.core.data.repository.DefaultScanRepository
import com.cherishmashankar.scanora.core.data.repository.ScanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindScanRepository(
        implementation: DefaultScanRepository
    ): ScanRepository
}