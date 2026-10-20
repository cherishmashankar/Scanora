package com.cherishmashankar.scanora.feature.scanner.presentation.result.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cherishmashankar.scanora.core.model.ScanContent
import com.cherishmashankar.scanora.feature.scanner.R

@Composable
internal fun ScanResultDataCard(
    content: ScanContent,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SelectionContainer {
                when (content) {
                    is ScanContent.Url -> {
                        ResultValue(content.url)
                    }

                    is ScanContent.Phone -> {
                        ResultValue(content.number)
                    }

                    is ScanContent.Text -> {
                        ResultValue(content.value)
                    }

                    is ScanContent.Wifi -> {
                        WifiContent(content)
                    }

                    is ScanContent.Email -> {
                        EmailContent(content)
                    }

                    is ScanContent.Contact -> {
                        ContactContent(content)
                    }
                }
            }
        }
    }
}

@Composable
private fun WifiContent(content: ScanContent.Wifi) {
    var passwordVisible by rememberSaveable(content.ssid) {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ResultFieldIfPresent(
            label = stringResource(R.string.result_network),
            value = content.ssid
        )

        ResultFieldIfPresent(
            label = stringResource(R.string.result_security),
            value = content.securityType
        )

        content.password
            ?.takeIf { it.isNotBlank() }
            ?.let { password ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.result_password),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = if (passwordVisible) {
                                password
                            } else {
                                "••••••••"
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) {
                                Icons.Outlined.VisibilityOff
                            } else {
                                Icons.Outlined.Visibility
                            },
                            contentDescription = stringResource(
                                if (passwordVisible) {
                                    R.string.result_hide_password
                                } else {
                                    R.string.result_show_password
                                }
                            )
                        )
                    }
                }
            }
    }
}

@Composable
private fun EmailContent(content: ScanContent.Email) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ResultFieldIfPresent(
            label = stringResource(R.string.result_email_address),
            value = content.address
        )

        ResultFieldIfPresent(
            label = stringResource(R.string.result_subject),
            value = content.subject
        )

        ResultFieldIfPresent(
            label = stringResource(R.string.result_message),
            value = content.body
        )
    }
}

@Composable
private fun ContactContent(content: ScanContent.Contact) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ResultFieldIfPresent(
            label = stringResource(R.string.result_name),
            value = content.name
        )

        ResultFieldIfPresent(
            label = stringResource(R.string.result_phone),
            value = content.phone
        )

        ResultFieldIfPresent(
            label = stringResource(R.string.result_email_address),
            value = content.email
        )
    }
}

@Composable
private fun ResultFieldIfPresent(
    label: String,
    value: String?
) {
    value
        ?.takeIf { it.isNotBlank() }
        ?.let { nonBlankValue ->
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                ResultValue(nonBlankValue)
            }
        }
}

@Composable
private fun ResultValue(value: String?) {
    value
        ?.takeIf { it.isNotBlank() }
        ?.let { nonBlankValue ->
            Text(
                text = nonBlankValue,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
}

@Preview(showBackground = true, name = "Website Content")
@Composable
private fun WebsiteContentPreview() {
    MaterialTheme {
        ScanResultDataCard(
            content = ScanContent.Url(
                url = "https://example.com"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, name = "Phone Content")
@Composable
private fun PhoneContentPreview() {
    MaterialTheme {
        ScanResultDataCard(
            content = ScanContent.Phone(
                number = "+49 123 456 789"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, name = "Wi-Fi Content")
@Composable
private fun WifiContentPreview() {
    MaterialTheme {
        ScanResultDataCard(
            content = ScanContent.Wifi(
                ssid = "ScanoraTest",
                password = "TestPassword123",
                securityType = "WPA2"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}