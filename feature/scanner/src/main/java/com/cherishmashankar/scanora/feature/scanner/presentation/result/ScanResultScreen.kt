package com.cherishmashankar.scanora.feature.scanner.presentation.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.feature.scanner.R

@Composable
internal fun ScanResultScreen(
    scan: Scan,
    onScanAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = scan.value,
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            onClick = onScanAgain,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.scan_again)
            )
        }
    }
}