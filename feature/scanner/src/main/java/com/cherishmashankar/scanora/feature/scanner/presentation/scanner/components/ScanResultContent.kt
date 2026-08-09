package com.cherishmashankar.scanora.feature.scanner.presentation.scanner.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.feature.scanner.R
import com.cherishmashankar.scanora.feature.scanner.presentation.scanner.toStringRes
import com.cherishmashankar.scanora.feature.scanner.presentation.toStringRes

@Composable
internal fun ScanResultContent(
    scan: Scan,
    onScanAgain: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = scan.value,
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(scan.format.toStringRes()),
            style = MaterialTheme.typography.bodyMedium
        )

        Button(
            onClick = onScanAgain
        ) {
            Text(
                text = stringResource(R.string.scan_again)
            )
        }
    }
}