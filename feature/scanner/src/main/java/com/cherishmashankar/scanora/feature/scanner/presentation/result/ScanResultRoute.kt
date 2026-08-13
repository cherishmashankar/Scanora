package com.cherishmashankar.scanora.feature.scanner.presentation.result

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cherishmashankar.scanora.feature.scanner.R
import com.cherishmashankar.scanora.feature.scanner.presentation.result.components.ScanResultLoading
import com.cherishmashankar.scanora.feature.scanner.presentation.result.components.ScanResultNotFound
import kotlinx.coroutines.launch



@Composable
internal fun ScanResultRoute(
    onBack: () -> Unit,
    viewModel: ScanResultViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isDeleting by viewModel.isDeleting.collectAsStateWithLifecycle()
    val deleteError by viewModel.deleteError.collectAsStateWithLifecycle()

    val snackbarHostState = remember {
        SnackbarHostState()
    }


    val coroutineScope = rememberCoroutineScope()

    val deleteFailedMessage =
        stringResource(R.string.scan_delete_failed)

    val openFailedMessage =
        stringResource(R.string.scan_open_failed)

    val shareFailedMessage =
        stringResource(R.string.scan_share_failed)

    LaunchedEffect(deleteError) {
        if (deleteError) {
            snackbarHostState.showSnackbar(
                message = deleteFailedMessage
            )

            viewModel.consumeDeleteError()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { innerPadding ->

        when (val state = uiState) {

            ScanResultUiState.Loading -> {
                ScanResultLoading(modifier = Modifier.padding(innerPadding))
            }

            is ScanResultUiState.Success -> {
                val scan = state.scan

                val canBrowse =
                    scan.value.isBrowsableUrl()

                ScanResultContent(
                    scan = scan,
                    showSaveAction = false,
                    showDeleteAction = true,
                    isSaved = true,
                    canBrowse = canBrowse,
                    onSave = {},
                    onDelete = {
                        if (!isDeleting) {
                            viewModel.deleteScan(
                                onDeleted = onBack
                            )
                        }
                    },
                    onShare = {
                        runCatching {
                            val shareIntent =
                                Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, scan.value)
                                }
                            context.startActivity(
                                Intent.createChooser(
                                    shareIntent,
                                    null
                                )
                            )
                        }.onFailure {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = shareFailedMessage
                                )
                            }
                        }
                    },
                    onBrowse = {
                        if (canBrowse) {
                            runCatching {
                                val browseIntent =
                                    Intent(Intent.ACTION_VIEW, scan.value.toBrowsableUrl().toUri())
                                context.startActivity(browseIntent)
                            }.onFailure {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = openFailedMessage
                                    )
                                }
                            }
                        }
                    },
                    onBottomAction = onBack,
                    modifier = Modifier.padding(
                        innerPadding
                    )
                )
            }

            ScanResultUiState.NotFound -> {
                ScanResultNotFound(
                    onBack = onBack,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}