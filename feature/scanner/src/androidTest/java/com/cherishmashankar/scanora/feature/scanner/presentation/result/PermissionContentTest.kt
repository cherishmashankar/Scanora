package com.cherishmashankar.scanora.feature.scanner.presentation.scanner.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cherishmashankar.scanora.feature.scanner.presentation.scanner.CameraPermissionState
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class PermissionContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun deniedShowsAllowCamera() {
        composeRule.setContent {
            PermissionContent(
                permissionState = CameraPermissionState.DENIED,
                onRequestPermission = {},
                onOpenSettings = {}
            )
        }

        composeRule
            .onNodeWithText("Allow camera")
            .assertIsDisplayed()
    }

    @Test
    fun deniedCallsRequestPermission() {
        var requested = false

        composeRule.setContent {
            PermissionContent(
                permissionState = CameraPermissionState.DENIED,
                onRequestPermission = { requested = true },
                onOpenSettings = {}
            )
        }

        composeRule
            .onNodeWithText("Allow camera")
            .performClick()

        assertTrue(requested)
    }

    @Test
    fun permanentlyDeniedShowsOpenSettings() {
        composeRule.setContent {
            PermissionContent(
                permissionState = CameraPermissionState.PERMANENTLY_DENIED,
                onRequestPermission = {},
                onOpenSettings = {}
            )
        }

        composeRule
            .onNodeWithText("Open settings")
            .assertIsDisplayed()
    }

    @Test
    fun permanentlyDeniedCallsOpenSettings() {
        var opened = false

        composeRule.setContent {
            PermissionContent(
                permissionState = CameraPermissionState.PERMANENTLY_DENIED,
                onRequestPermission = {},
                onOpenSettings = { opened = true }
            )
        }

        composeRule
            .onNodeWithText("Open settings")
            .performClick()

        assertTrue(opened)
    }
}