package com.cherishmashankar.scanora.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cherishmashankar.scanora.MainActivity
import org.junit.Rule
import org.junit.Test

internal class ScanoraNavigationSmokeTest {

    @get:Rule
    val composeRule =
        createAndroidComposeRule<MainActivity>()

    @Test
    fun scannerAndHistoryNavigationWorks() {
        composeRule
            .onNodeWithTag("scanner_screen")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("History")
            .performClick()

        composeRule
            .onNodeWithTag("history_title")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Scanner")
            .performClick()

        composeRule
            .onNodeWithTag("scanner_screen")
            .assertIsDisplayed()
    }


    @Test
    fun switchingTabsRepeatedlyDoesNotBreakNavigation() {
        composeRule
            .onNodeWithText("History")
            .performClick()

        composeRule
            .onNodeWithTag("history_title")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Scanner")
            .performClick()

        composeRule
            .onNodeWithTag("scanner_screen")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("History")
            .performClick()

        composeRule
            .onNodeWithTag("history_title")
            .assertIsDisplayed()
    }
}