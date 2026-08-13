package com.cherishmashankar.scanora.feature.scanner.presentation.result.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import com.cherishmashankar.scanora.core.model.ScanFormat
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import androidx.core.graphics.set

@Composable
internal fun ScanCodePreview(
    value: String,
    format: ScanFormat,
    modifier: Modifier = Modifier
) {
    val bitmap = remember(value, format) {
        generateCodeBitmap(
            value = value,
            format = format
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = when (format) {
                ScanFormat.QR_CODE -> Modifier.size(180.dp)
                ScanFormat.BARCODE -> Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            }
        )
    }
}

private fun generateCodeBitmap(
    value: String,
    format: ScanFormat
): Bitmap {
    val barcodeFormat = when (format) {
        ScanFormat.QR_CODE -> BarcodeFormat.QR_CODE
        ScanFormat.BARCODE -> BarcodeFormat.CODE_128
    }

    val width = if (format == ScanFormat.QR_CODE) 512 else 900
    val height = if (format == ScanFormat.QR_CODE) 512 else 300

    val matrix = MultiFormatWriter().encode(
        value,
        barcodeFormat,
        width,
        height
    )

    return createBitmap(
        width = width,
        height = height,
        config = Bitmap.Config.ARGB_8888
    ).apply {
        for (x in 0 until width) {
            for (y in 0 until height) {
                this[x, y] = if (matrix[x, y]) {
                    android.graphics.Color.BLACK
                } else {
                    android.graphics.Color.WHITE
                }
            }
        }
    }
}