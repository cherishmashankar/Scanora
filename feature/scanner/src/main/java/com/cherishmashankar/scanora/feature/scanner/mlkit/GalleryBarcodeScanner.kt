package com.cherishmashankar.scanora.feature.scanner.mlkit

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await


internal class GalleryBarcodeScanner @Inject constructor(
    @param:ApplicationContext private val context: Context
) : GalleryImageScanner {

    override suspend fun scan(uri: Uri): List<Barcode> {
        val scanner = createBarcodeScanner()

        return try {
            val image = InputImage.fromFilePath(context, uri)
            scanner.process(image).await()
        } finally {
            scanner.close()
        }
    }
}