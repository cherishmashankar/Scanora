package com.cherishmashankar.scanora.feature.scanner.presentation.result.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.feature.scanner.R

@Composable
internal fun ScanResultBottomActions(
    isSaved: Boolean,
    onSave: () -> Unit,
    onScanAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onSave,
            enabled = !isSaved,
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary,
                disabledContainerColor = colorScheme.primaryContainer,
                disabledContentColor = colorScheme.onPrimaryContainer
            )
        ) {
            Icon(
                imageVector = if (isSaved) {
                    Icons.Default.Check
                } else {
                    Icons.Default.Save
                },
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = stringResource(
                    if (isSaved) R.string.saved else R.string.save
                ),
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1
            )
        }

        OutlinedButton(
            onClick = onScanAgain,
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.QrCodeScanner,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = stringResource(R.string.scan_again),
                style = MaterialTheme.typography.labelLarge,
                maxLines = 2
            )
        }
    }
}

@Preview(
    name = "New Scan - Save Highlighted",
    showBackground = true
)
@Composable
private fun ScanResultBottomActionsPreview() {
    MaterialTheme {
        ScanResultBottomActions(
            isSaved = false,
            onSave = {},
            onScanAgain = {},
            modifier = Modifier
        )
    }
}

@Preview(
    name = "Already Saved",
    showBackground = true
)
@Composable
private fun ScanResultBottomActionsSavedPreview() {
    MaterialTheme {
        ScanResultBottomActions(
            isSaved = true,
            onSave = {},
            onScanAgain = {},
            modifier = Modifier
        )
    }
}