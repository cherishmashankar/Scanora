package com.cherishmashankar.scanora.feature.scanner.presentation.result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.core.model.Scan
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
            onDelete = onDelete,
            onShare = onShare
        )

        Spacer(Modifier.height(12.dp))

        ScanFormatBadge(
            format = scan.format
        )

        Spacer(Modifier.height(24.dp))

        ScanContentCard(
            value = scan.value
        )

        Spacer(Modifier.height(24.dp))

        ScanResultActions(
            canBrowse = canBrowse,
            onBrowse = onBrowse,
            bottomActionText = bottomActionText,
            onBottomAction = onBottomAction,
            modifier = Modifier.weight(1f)
        )
    }
}