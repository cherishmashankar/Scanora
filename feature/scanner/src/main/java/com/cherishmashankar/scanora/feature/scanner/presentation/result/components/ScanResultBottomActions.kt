package com.cherishmashankar.scanora.feature.scanner.presentation.result.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.feature.scanner.R

@Composable
internal fun ScanResultBottomActions(
    isSaved: Boolean,
    onSave: () -> Unit,
    onScanAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onScanAgain,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = stringResource(R.string.scan_again)
            )
        }

        Button(
            onClick = onSave,
            enabled = !isSaved,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = if (isSaved) {
                    Icons.Default.Check
                } else {
                    Icons.Default.Save
                },
                contentDescription = null
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = stringResource(
                    if (isSaved) R.string.saved else R.string.save
                )
            )
        }
    }
}