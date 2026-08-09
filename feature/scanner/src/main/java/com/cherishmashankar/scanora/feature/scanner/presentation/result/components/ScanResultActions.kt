package com.cherishmashankar.scanora.feature.scanner.presentation.result.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.feature.scanner.R

@Composable
internal fun ScanResultActions(
    canBrowse: Boolean,
    onBrowse: () -> Unit,
    bottomActionText: String,
    onBottomAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        if (canBrowse) {
            Button(
                onClick = onBrowse,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.open_in_browser))

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = null
                )
            }
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        TextButton(
            onClick = onBottomAction,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = bottomActionText
            )
        }
    }
}