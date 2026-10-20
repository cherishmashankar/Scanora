package com.cherishmashankar.scanora.feature.scanner.presentation.result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.core.model.ScanContent
import com.cherishmashankar.scanora.core.model.ScanFormat
import com.cherishmashankar.scanora.feature.scanner.R
import com.cherishmashankar.scanora.feature.scanner.presentation.result.components.FlippableScanResultHeader
import com.cherishmashankar.scanora.feature.scanner.presentation.result.components.ScanCodePreview
import com.cherishmashankar.scanora.feature.scanner.presentation.result.components.ScanResultBottomActions
import com.cherishmashankar.scanora.feature.scanner.presentation.result.components.ScanResultDataCard
import com.cherishmashankar.scanora.feature.scanner.presentation.result.components.ScanResultHeader
import com.cherishmashankar.scanora.feature.scanner.presentation.result.components.ScanResultPrimaryAction
import com.cherishmashankar.scanora.feature.scanner.presentation.result.components.ScanResultType

@Composable
internal fun ScanResultContent(
    scan: Scan,
    content: ScanContent,
    actions: List<SmartAction>,
    onSmartAction: (SmartAction) -> Unit,
    showSaveAction: Boolean,
    showDeleteAction: Boolean,
    isSaved: Boolean,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    onBottomAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by rememberSaveable {
        mutableStateOf(false)
    }


    val resultType = when (content) {
        is ScanContent.Url -> ScanResultType.URL
        is ScanContent.Phone -> ScanResultType.PHONE
        is ScanContent.Wifi -> ScanResultType.WIFI
        is ScanContent.Email -> ScanResultType.EMAIL
        is ScanContent.Contact -> ScanResultType.CONTACT
        is ScanContent.Text -> ScanResultType.TEXT
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            ScanResultHeader(
                showDeleteAction = showDeleteAction,
                onCopy = {
                    onSmartAction(SmartAction.COPY)
                },
                onShare = onShare,
                onDelete = {
                    showDeleteDialog = true
                }
            )

            Spacer(Modifier.height(20.dp))

            FlippableScanResultHeader(
                type = resultType,
                codePreview = {
                    ScanCodePreview(
                        value = scan.value,
                        format = scan.format
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            ScanResultDataCard(
                content = content
            )

            Spacer(Modifier.height(20.dp))

            ScanResultPrimaryAction(
                actions = actions,
                onSmartAction = onSmartAction
            )

            Spacer(
                modifier = Modifier
                    .weight(1f, fill = true)
                    .height(28.dp)
            )

            if (showSaveAction) {
                ScanResultBottomActions(
                    isSaved = isSaved,
                    onSave = onSave,
                    onScanAgain = onBottomAction
                )
            }

            Spacer(Modifier.height(20.dp))
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text(
                    text = stringResource(R.string.delete_scan_title)
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.delete_scan_message)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
            }
        )
    }
}

@Preview(
    name = "Website Result - New Scan",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun ScanResultContentWebsitePreview() {
    MaterialTheme {
        ScanResultContent(
            scan = Scan(
                value = "https://example.com",
                format = ScanFormat.QR_CODE,
                timestamp = 0L
            ),
            content = ScanContent.Url(
                url = "https://example.com"
            ),
            actions = listOf(
                SmartAction.OPEN_URL,
                SmartAction.COPY,
                SmartAction.SHARE
            ),
            onSmartAction = {},
            showSaveAction = true,
            showDeleteAction = false,
            isSaved = false,
            onSave = {},
            onDelete = {},
            onShare = {},
            onBottomAction = {}
        )
    }
}

@Preview(
    name = "Website Result - Saved",
    showBackground = true
)
@Composable
private fun ScanResultContentSavedPreview() {
    MaterialTheme {
        ScanResultContent(
            scan = Scan(
                id = 1L,
                value = "https://example.com",
                format = ScanFormat.QR_CODE,
                timestamp = 0L
            ),
            content = ScanContent.Url(
                url = "https://example.com"
            ),
            actions = listOf(
                SmartAction.OPEN_URL,
                SmartAction.COPY,
                SmartAction.SHARE
            ),
            onSmartAction = {},
            showSaveAction = true,
            showDeleteAction = false,
            isSaved = true,
            onSave = {},
            onDelete = {},
            onShare = {},
            onBottomAction = {}
        )
    }
}

@Preview(
    name = "Website Result - History",
    showBackground = true
)
@Composable
private fun ScanResultContentHistoryPreview() {
    MaterialTheme {
        ScanResultContent(
            scan = Scan(
                id = 1L,
                value = "https://example.com",
                format = ScanFormat.QR_CODE,
                timestamp = 0L
            ),
            content = ScanContent.Url(
                url = "https://example.com"
            ),
            actions = listOf(
                SmartAction.OPEN_URL,
                SmartAction.COPY,
                SmartAction.SHARE
            ),
            onSmartAction = {},
            showSaveAction = false,
            showDeleteAction = true,
            isSaved = true,
            onSave = {},
            onDelete = {},
            onShare = {},
            onBottomAction = {}
        )
    }
}