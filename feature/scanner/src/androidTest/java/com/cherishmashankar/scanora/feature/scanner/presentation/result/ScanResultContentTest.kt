package com.cherishmashankar.scanora.feature.scanner.presentation.result

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.core.model.ScanFormat
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

internal class ScanResultContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun freshResultShowsSave() {
        composeRule.setContent {
            ScanResultContent(
                scan = testScan(),
                showSaveAction = true,
                showDeleteAction = false,
                isSaved = false,
                canBrowse = true,
                onSave = {},
                onDelete = {},
                onShare = {},
                onBrowse = {},
                onBottomAction = {}
            )
        }

        composeRule
            .onNodeWithText("Save")
            .assertIsDisplayed()
    }

    @Test
    fun savedHistoryResultDoesNotShowSave() {
        composeRule.setContent {
            ScanResultContent(
                scan = testScan(),
                showSaveAction = false,
                showDeleteAction = true,
                isSaved = true,
                canBrowse = true,
                onSave = {},
                onDelete = {},
                onShare = {},
                onBrowse = {},
                onBottomAction = {}
            )
        }

        composeRule
            .onNodeWithText("Save")
            .assertDoesNotExist()
    }

    @Test
    fun freshResultShowsScanAgain() {
        composeRule.setContent {
            ScanResultContent(
                scan = testScan(),
                showSaveAction = true,
                showDeleteAction = false,
                isSaved = false,
                canBrowse = true,
                onSave = {},
                onDelete = {},
                onShare = {},
                onBrowse = {},
                onBottomAction = {}
            )
        }

        composeRule
            .onNodeWithText("Scan again")
            .assertIsDisplayed()
    }

    @Test
    fun saveButtonCallsSaveCallback() {
        var saved = false

        composeRule.setContent {
            ScanResultContent(
                scan = testScan(),
                showSaveAction = true,
                showDeleteAction = false,
                isSaved = false,
                canBrowse = true,
                onSave = {
                    saved = true
                },
                onDelete = {},
                onShare = {},
                onBrowse = {},
                onBottomAction = {}
            )
        }

        composeRule
            .onNodeWithText("Save")
            .performClick()

        Assert.assertTrue(saved)
    }

    @Test
    fun deleteOpensConfirmationDialog() {
        composeRule.setContent {
            ScanResultContent(
                scan = testScan(),
                showSaveAction = false,
                showDeleteAction = true,
                isSaved = true,
                canBrowse = false,
                onSave = {},
                onDelete = {},
                onShare = {},
                onBrowse = {},
                onBottomAction = {}
            )
        }

        composeRule
            .onNodeWithContentDescription("Delete")
            .performClick()

        composeRule
            .onNodeWithText("Delete scan?")
            .assertIsDisplayed()
    }

    @Test
    fun confirmingDeleteCallsCallback() {
        var deleted = false

        composeRule.setContent {
            ScanResultContent(
                scan = testScan(),
                showSaveAction = false,
                showDeleteAction = true,
                isSaved = true,
                canBrowse = false,
                onSave = {},
                onDelete = {
                    deleted = true
                },
                onShare = {},
                onBrowse = {},
                onBottomAction = {}
            )
        }

        composeRule
            .onNodeWithContentDescription("Delete")
            .performClick()

        composeRule
            .onNodeWithText("Delete")
            .performClick()

        Assert.assertTrue(deleted)
    }

    @Test
    fun nonBrowsableContentHasNoOpenAction() {
        composeRule.setContent {
            ScanResultContent(
                scan = testScan(
                    value = "123456789"
                ),
                showSaveAction = true,
                showDeleteAction = false,
                isSaved = false,
                canBrowse = false,
                onSave = {},
                onDelete = {},
                onShare = {},
                onBrowse = {},
                onBottomAction = {}
            )
        }

        composeRule
            .onNodeWithContentDescription("Open in browser")
            .assertDoesNotExist()
    }

    private fun testScan(
        value: String = "https://example.com"
    ) = Scan(
        id = 42L,
        value = value,
        format = ScanFormat.QR_CODE,
        timestamp = 1L
    )
}