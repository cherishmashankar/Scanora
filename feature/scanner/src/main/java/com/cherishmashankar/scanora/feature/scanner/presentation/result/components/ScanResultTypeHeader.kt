package com.cherishmashankar.scanora.feature.scanner.presentation.result.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.feature.scanner.R

internal enum class ScanResultType(
    @StringRes val headingRes: Int,
    @StringRes val subtitleRes: Int,
    val icon: ImageVector
) {
    URL(
        R.string.result_website_detected,
        R.string.result_type_url,
        Icons.Outlined.Language
    ),
    PHONE(
        R.string.result_phone_detected,
        R.string.result_type_phone,
        Icons.Outlined.Phone
    ),
    WIFI(
        R.string.result_wifi_detected,
        R.string.result_type_wifi,
        Icons.Outlined.Wifi
    ),
    EMAIL(
        R.string.result_email_detected,
        R.string.result_type_email,
        Icons.Outlined.AlternateEmail
    ),
    CONTACT(
        R.string.result_contact_detected,
        R.string.result_type_contact,
        Icons.Outlined.Contacts
    ),
    TEXT(
        R.string.result_text_detected,
        R.string.result_type_text,
        Icons.Outlined.Description
    )
}

@Composable
internal fun ScanResultTypeHeader(
    type: ScanResultType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = type.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    RoundedCornerShape(20.dp)
                )
                .padding(18.dp)
                .size(32.dp)
        )

        Text(
            text = stringResource(type.headingRes),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(type.subtitleRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, name = "Website Result")
@Composable
private fun WebsitePreview() {
    MaterialTheme {
        ScanResultTypeHeader(ScanResultType.URL)
    }
}

@Preview(showBackground = true, name = "Wi-Fi Result")
@Composable
private fun WifiPreview() {
    MaterialTheme {
        ScanResultTypeHeader(ScanResultType.WIFI)
    }
}

@Preview(showBackground = true, name = "Phone Result")
@Composable
private fun PhonePreview() {
    MaterialTheme {
        ScanResultTypeHeader(ScanResultType.PHONE)
    }
}