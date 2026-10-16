package com.cherishmashankar.scanora.feature.scanner.mlkit

import android.net.Uri
import com.google.mlkit.vision.barcode.common.Barcode

internal interface GalleryImageScanner {
    suspend fun scan(uri: Uri): List<Barcode>
}