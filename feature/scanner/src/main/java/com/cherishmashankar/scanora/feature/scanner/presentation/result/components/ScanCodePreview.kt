package com.cherishmashankar.scanora.feature.scanner.presentation.result.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.core.model.ScanFormat
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import java.util.EnumMap
import androidx.core.graphics.createBitmap

@Composable
internal fun ScanCodePreview(
    value: String,
    format: ScanFormat,
    modifier: Modifier = Modifier
) {
    val barcodeFormat = remember(format) {
        barcodeFormatFor(format)
    }

    val bitmap = remember(value, barcodeFormat) {
        barcodeFormat?.let {
            generateBarcodeBitmap(
                value = value,
                format = it
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            val isTwoDimensional = barcodeFormat in setOf(
                BarcodeFormat.QR_CODE,
                BarcodeFormat.DATA_MATRIX,
                BarcodeFormat.AZTEC,
                BarcodeFormat.PDF_417
            )

            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Scanned code preview",
                modifier = if (isTwoDimensional) {
                    Modifier
                        .size(180.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                } else {
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                },
                contentScale = ContentScale.Fit
            )
        } else {
            Text(
                text = "Code preview unavailable",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun barcodeFormatFor(
    format: ScanFormat
): BarcodeFormat? {
    return when (format.name) {
        "QR_CODE" -> BarcodeFormat.QR_CODE
        "AZTEC" -> BarcodeFormat.AZTEC
        "DATA_MATRIX" -> BarcodeFormat.DATA_MATRIX
        "PDF417", "PDF_417" -> BarcodeFormat.PDF_417

        "EAN_13" -> BarcodeFormat.EAN_13
        "EAN_8" -> BarcodeFormat.EAN_8
        "UPC_A" -> BarcodeFormat.UPC_A
        "UPC_E" -> BarcodeFormat.UPC_E

        "CODE_39" -> BarcodeFormat.CODE_39
        "CODE_93" -> BarcodeFormat.CODE_93
        "CODE_128" -> BarcodeFormat.CODE_128
        "CODABAR" -> BarcodeFormat.CODABAR
        "ITF" -> BarcodeFormat.ITF

        else -> null
    }
}

private fun generateBarcodeBitmap(
    value: String,
    format: BarcodeFormat
): Bitmap? {
    if (value.isBlank()) return null

    return runCatching {
        val isTwoDimensional = format in setOf(
            BarcodeFormat.QR_CODE,
            BarcodeFormat.DATA_MATRIX,
            BarcodeFormat.AZTEC,
            BarcodeFormat.PDF_417
        )

        val width = if (isTwoDimensional) 600 else 900
        val height = if (isTwoDimensional) 600 else 240

        val hints = EnumMap<EncodeHintType, Any>(
            EncodeHintType::class.java
        ).apply {
            put(EncodeHintType.MARGIN, 2)
            put(EncodeHintType.CHARACTER_SET, "UTF-8")
        }

        val matrix = MultiFormatWriter().encode(
            value,
            format,
            width,
            height,
            hints
        )

        val pixels = IntArray(
            matrix.width * matrix.height
        )

        for (y in 0 until matrix.height) {
            for (x in 0 until matrix.width) {
                pixels[y * matrix.width + x] =
                    if (matrix[x, y]) {
                        android.graphics.Color.BLACK
                    } else {
                        android.graphics.Color.WHITE
                    }
            }
        }

        createBitmap(matrix.width, matrix.height).apply {
            setPixels(
                pixels,
                0,
                matrix.width,
                0,
                0,
                matrix.width,
                matrix.height
            )
        }
    }.getOrNull()
}

@Preview(
    name = "QR Code Preview",
    showBackground = true
)
@Composable
private fun ScanCodePreviewQrPreview() {
    MaterialTheme {
        ScanCodePreview(
            value = "https://example.com",
            format = ScanFormat.QR_CODE
        )
    }
}