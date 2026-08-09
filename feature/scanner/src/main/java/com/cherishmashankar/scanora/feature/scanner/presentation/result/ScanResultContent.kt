package com.cherishmashankar.scanora.feature.scanner.presentation.result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.feature.scanner.R
import com.cherishmashankar.scanora.feature.scanner.presentation.result.components.ScanContentCard
import com.cherishmashankar.scanora.feature.scanner.presentation.result.components.ScanFormatBadge
import com.cherishmashankar.scanora.feature.scanner.presentation.result.components.ScanResultActions
import com.cherishmashankar.scanora.feature.scanner.presentation.result.components.ScanResultHeader

@Composable
internal fun ScanResultContent(
    scan: Scan,
    showSaveAction: Boolean,
    showDeleteAction: Boolean,
    isSaved: Boolean,
    canBrowse: Boolean,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    onBrowse: () -> Unit,
    bottomActionText: String,
    onBottomAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        ScanResultHeader(
            showSaveAction = showSaveAction,
            showDeleteAction = showDeleteAction,
            isSaved = isSaved,
            onSave = onSave,
            onDelete = { showDeleteDialog = true },
            onShare = onShare
        )

        Spacer(Modifier.height(12.dp))

        ScanFormatBadge(format = scan.format)

        Spacer(Modifier.height(24.dp))

        ScanContentCard(value = scan.value)

        Spacer(Modifier.height(24.dp))

        ScanResultActions(
            canBrowse = canBrowse,
            onBrowse = onBrowse,
            bottomActionText = bottomActionText,
            onBottomAction = onBottomAction,
            modifier = Modifier.weight(1f)
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(stringResource(R.string.delete_scan_title))
            },
            text = {
                Text(stringResource(R.string.delete_scan_message))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}