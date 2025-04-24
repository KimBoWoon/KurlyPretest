package com.bowoon.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.bowoon.ui.components.PagingAppendErrorComponent
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class PagingAppendErrorTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showPagingAppendErrorTest() {
        composeTestRule.apply {
            setContent {
                PagingAppendErrorComponent(
                    retry = {}
                )
            }

            onNodeWithContentDescription(label = "pagingAppendError").assertExists()
            onNodeWithText(text = "재시도").assertExists().assertIsDisplayed()
        }
    }

    @Test
    fun retryClickTest() {
        composeTestRule.apply {
            var doRetry = false

            setContent {
                PagingAppendErrorComponent(
                    retry = { doRetry = true }
                )
            }

            assertEquals(doRetry, false)
            onNodeWithContentDescription(label = "pagingAppendError").assertExists()
            onNodeWithText(text = "재시도").assertExists().assertIsDisplayed().performClick()
            assertEquals(doRetry, true)
        }
    }
}