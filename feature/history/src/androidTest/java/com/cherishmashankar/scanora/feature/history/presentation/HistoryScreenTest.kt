package com.cherishmashankar.scanora.feature.history.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.cherishmashankar.scanora.core.model.Scan
import com.cherishmashankar.scanora.core.model.ScanFormat
import org.junit.Rule
import org.junit.Test

internal class HistoryScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun historyTitleIsDisplayed() {
        composeRule.setContent {
            HistoryScreen(
                uiState = HistoryUiState(),
                onScanClick = {},
                onSearchClick = {},
                onSearchQueryChanged = {},
                onCloseSearch = {}
            )
        }

        composeRule
            .onNodeWithText("History")
            .assertIsDisplayed()
    }

    @Test
    fun savedScanIsDisplayed() {
        composeRule.setContent {
            HistoryScreen(
                uiState = HistoryUiState(
                    scans = listOf(testScan())
                ),
                onScanClick = {},
                onSearchClick = {},
                onSearchQueryChanged = {},
                onCloseSearch = {}
            )
        }

        composeRule
            .onNodeWithText("https://example.com")
            .assertIsDisplayed()
    }

    @Test
    fun emptyHistoryShowsEmptyState() {
        composeRule.setContent {
            HistoryScreen(
                uiState = HistoryUiState(),
                onScanClick = {},
                onSearchClick = {},
                onSearchQueryChanged = {},
                onCloseSearch = {}
            )
        }

        composeRule
            .onNodeWithText("No saved scans yet")
            .assertIsDisplayed()
    }

    @Test
    fun searchModeShowsSearchField() {
        composeRule.setContent {
            HistoryScreen(
                uiState = HistoryUiState(
                    isSearchActive = true
                ),
                onScanClick = {},
                onSearchClick = {},
                onSearchQueryChanged = {},
                onCloseSearch = {}
            )
        }

        composeRule
            .onNodeWithText("Search saved scans")
            .assertIsDisplayed()
    }

    @Test
    fun typingSearchCallsCallback() {
        var query = ""

        composeRule.setContent {
            HistoryScreen(
                uiState = HistoryUiState(
                    isSearchActive = true,
                    searchQuery = query
                ),
                onScanClick = {},
                onSearchClick = {},
                onSearchQueryChanged = {
                    query = it
                },
                onCloseSearch = {}
            )
        }

        composeRule
            .onNodeWithText("Search saved scans")
            .performTextInput("google")

        assert(query == "google")
    }

    @Test
    fun noSearchResultsShowsNoResults() {
        composeRule.setContent {
            HistoryScreen(
                uiState = HistoryUiState(
                    scans = emptyList(),
                    searchQuery = "missing",
                    isSearchActive = true
                ),
                onScanClick = {},
                onSearchClick = {},
                onSearchQueryChanged = {},
                onCloseSearch = {}
            )
        }

        composeRule
            .onNodeWithText("No results")
            .assertIsDisplayed()
    }

    @Test
    fun clickingScanCallsCallback() {
        var clickedId: Long? = null

        composeRule.setContent {
            HistoryScreen(
                uiState = HistoryUiState(
                    scans = listOf(testScan())
                ),
                onScanClick = {
                    clickedId = it
                },
                onSearchClick = {},
                onSearchQueryChanged = {},
                onCloseSearch = {}
            )
        }

        composeRule
            .onNodeWithText("https://example.com")
            .performClick()

        assert(clickedId == 42L)
    }

    private fun testScan() = Scan(
        id = 42L,
        value = "https://example.com",
        format = ScanFormat.QR_CODE,
        timestamp = 1L
    )
}