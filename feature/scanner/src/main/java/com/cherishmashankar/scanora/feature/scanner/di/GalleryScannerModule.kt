package com.cherishmashankar.scanora.feature.scanner.di

import com.cherishmashankar.scanora.feature.scanner.mlkit.GalleryBarcodeScanner
import com.cherishmashankar.scanora.feature.scanner.mlkit.GalleryImageScanner
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class GalleryScannerModule {

    @Binds
    abstract fun bindGalleryImageScanner(
        implementation: GalleryBarcodeScanner
    ): GalleryImageScanner
}