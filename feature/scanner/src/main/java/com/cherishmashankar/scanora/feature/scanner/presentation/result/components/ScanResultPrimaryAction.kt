package com.cherishmashankar.scanora.feature.scanner.presentation.result.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.ContactPage
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.feature.scanner.R
import com.cherishmashankar.scanora.feature.scanner.presentation.result.SmartAction

@Composable
internal fun ScanResultPrimaryAction(
    actions: List<SmartAction>,
    onSmartAction: (SmartAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryAction = actions.firstOrNull { action ->
        action != SmartAction.COPY &&
                action != SmartAction.SHARE
    } ?: return

    val presentation = primaryAction.presentation() ?: return

    Button(
        onClick = { onSmartAction(primaryAction) },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        shape = MaterialTheme.shapes.large,

    ) {
        Icon(
            imageVector = presentation.icon,
            contentDescription = null
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.width(10.dp)
        )

        Text(
            text = stringResource(presentation.labelRes),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

private data class ActionPresentation(
    @param:StringRes val labelRes: Int,
    val icon: ImageVector
)

private fun SmartAction.presentation(): ActionPresentation? =
    when (this) {
        SmartAction.OPEN_URL -> ActionPresentation(
            labelRes = R.string.result_action_open_website,
            icon = Icons.AutoMirrored.Outlined.OpenInNew
        )

        SmartAction.CONNECT_WIFI -> ActionPresentation(
            labelRes = R.string.result_action_wifi,
            icon = Icons.Outlined.Wifi
        )

        SmartAction.SEND_EMAIL -> ActionPresentation(
            labelRes = R.string.result_action_send_email,
            icon = Icons.AutoMirrored.Outlined.Send
        )

        SmartAction.DIAL_PHONE -> ActionPresentation(
            labelRes = R.string.result_action_dial,
            icon = Icons.Outlined.Phone
        )

        SmartAction.ADD_CONTACT -> ActionPresentation(
            labelRes = R.string.result_action_add_contact,
            icon = Icons.Outlined.ContactPage
        )

        SmartAction.COPY,
        SmartAction.SHARE -> null
    }

// --- Previews ---

@Preview(
    name = "Website Action",
    showBackground = true
)
@Composable
private fun WebsiteActionPreview() {
    MaterialTheme {
        ScanResultPrimaryAction(
            actions = listOf(
                SmartAction.OPEN_URL,
                SmartAction.COPY,
                SmartAction.SHARE
            ),
            onSmartAction = {}
        )
    }
}

@Preview(
    name = "Phone Action",
    showBackground = true
)
@Composable
private fun PhoneActionPreview() {
    MaterialTheme {
        ScanResultPrimaryAction(
            actions = listOf(
                SmartAction.DIAL_PHONE,
                SmartAction.COPY,
                SmartAction.SHARE
            ),
            onSmartAction = {}
        )
    }
}

@Preview(
    name = "Wi-Fi Action",
    showBackground = true
)
@Composable
private fun WifiActionPreview() {
    MaterialTheme {
        ScanResultPrimaryAction(
            actions = listOf(
                SmartAction.CONNECT_WIFI,
                SmartAction.COPY,
                SmartAction.SHARE
            ),
            onSmartAction = {}
        )
    }
}