package com.cherishmashankar.scanora.feature.scanner.presentation.result.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.cherishmashankar.scanora.feature.scanner.R
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview


@Composable
internal fun ScanResultHeader(
    showDeleteAction: Boolean,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    isCopied: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.scan_result_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCopy) {
                Icon(
                    imageVector = if (isCopied) {
                        Icons.Default.Check
                    } else {
                        Icons.Default.ContentCopy
                    },
                    contentDescription = if (isCopied) {
                        "Copied to clipboard"
                    } else {
                        "Copy scanned content"
                    },
                    tint = if (isCopied) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            IconButton(onClick = onShare) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = stringResource(R.string.share),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (showDeleteAction) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


@Preview(
    name = "New Scan Result",
    showBackground = true
)
@Composable
private fun ScanResultHeaderNewScanPreview() {
    MaterialTheme {
        Surface {
            ScanResultHeader(
                showDeleteAction = false,
                onCopy = {},
                onShare = {},
                onDelete = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(
    name = "Saved History Result",
    showBackground = true
)
@Composable
private fun ScanResultHeaderHistoryPreview() {
    MaterialTheme {
        Surface {
            ScanResultHeader(
                showDeleteAction = true,
                onCopy = {},
                onShare = {},
                onDelete = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}