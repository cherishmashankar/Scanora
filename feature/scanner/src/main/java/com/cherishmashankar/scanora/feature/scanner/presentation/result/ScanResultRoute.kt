package com.cherishmashankar.scanora.feature.scanner.presentation.result

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cherishmashankar.scanora.feature.scanner.R

@Composable
internal fun ScanResultRoute(
    onBack: () -> Unit,
    viewModel: ScanResultViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {

        ScanResultUiState.Loading -> {
            Text(
                text = stringResource(
                    R.string.scan_result_loading
                )
            )
        }

        is ScanResultUiState.Success -> {
            val scan = state.scan

            val canBrowse =
                scan.value.isBrowsableUrl()

            ScanResultContent(
                scan = scan,

                // This scan came from Room/History,
                // so it is already saved.
                showSaveAction = false,
                showDeleteAction = true,
                isSaved = true,

                canBrowse = canBrowse,

                onSave = {
                    // No save action needed for History item.
                },
                onDelete = {
                    viewModel.deleteScan(
                        onDeleted = onBack
                    )
                },

                onShare = {
                    val shareIntent =
                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"

                            putExtra(
                                Intent.EXTRA_TEXT,
                                scan.value
                            )
                        }

                    context.startActivity(
                        Intent.createChooser(
                            shareIntent,
                            null
                        )
                    )
                },

                onBrowse = {
                    if (canBrowse) {
                        val browseIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(scan.value)
                        )

                        context.startActivity(
                            browseIntent
                        )
                    }
                },

                bottomActionText = stringResource(
                    R.string.back
                ),

                onBottomAction = onBack
            )
        }

        ScanResultUiState.NotFound -> {
            Text(
                text = stringResource(
                    R.string.scan_result_not_found
                )
            )
        }
    }
}