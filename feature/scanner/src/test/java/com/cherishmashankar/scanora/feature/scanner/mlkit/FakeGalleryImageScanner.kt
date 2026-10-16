package com.cherishmashankar.scanora.feature.scanner.presentation.mlkit

import android.net.Uri
import com.cherishmashankar.scanora.feature.scanner.mlkit.GalleryImageScanner
import com.google.mlkit.vision.barcode.common.Barcode

internal class FakeGalleryImageScanner : GalleryImageScanner {

    var barcodes: List<Barcode> = emptyList()
    var exception: Exception? = null

    override suspend fun scan(uri: Uri): List<Barcode> {
        exception?.let { throw it }
        return barcodes
    }
}