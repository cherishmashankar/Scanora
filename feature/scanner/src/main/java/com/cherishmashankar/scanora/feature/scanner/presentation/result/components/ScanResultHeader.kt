package com.cherishmashankar.scanora.feature.scanner.presentation.result.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
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

@Composable
internal fun ScanResultHeader(
    showSaveAction: Boolean,
    showDeleteAction: Boolean,
    isSaved: Boolean,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.scan_result_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Row {
            if (showSaveAction) {
                IconButton(
                    onClick = onSave,
                    enabled = !isSaved
                ) {
                    Icon(
                        imageVector = if (isSaved) {
                            Icons.Default.Check
                        } else {
                            Icons.Default.Save
                        },
                        contentDescription = stringResource(
                            if (isSaved) R.string.saved else R.string.save
                        )
                    )
                }
            }

            IconButton(
                onClick = onShare
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = stringResource(R.string.share)
                )
            }

            if (showDeleteAction) {
                IconButton(
                    onClick = onDelete
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}