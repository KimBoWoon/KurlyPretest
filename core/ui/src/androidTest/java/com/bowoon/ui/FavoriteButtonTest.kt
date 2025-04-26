package com.bowoon.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.bowoon.ui.components.FavoriteButtonComponent
import org.junit.Rule
import org.junit.Test

class FavoriteButtonTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showFavoriteButtonTest() {
        composeTestRule.apply {
            setContent {
                FavoriteButtonComponent(
                    isFavorite = true
                )
            }

            onNodeWithContentDescription(label = "favorite").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "unFavorite").assertDoesNotExist()
        }
    }

    @Test
    fun showUnFavoriteButtonTest() {
        composeTestRule.apply {
            setContent {
                FavoriteButtonComponent(
                    isFavorite = false
                )
            }

            onNodeWithContentDescription(label = "unFavorite").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "favorite").assertDoesNotExist()
        }
    }

    @Test
    fun showFavoriteButtonClickTest() {
        composeTestRule.apply {
            setContent {
                var isFavorite by remember { mutableStateOf(true) }

                if (isFavorite) {
                    FavoriteButtonComponent(
                        modifier = Modifier.clickable { isFavorite = false },
                        isFavorite = isFavorite
                    )
                } else {
                    FavoriteButtonComponent(
                        modifier = Modifier.clickable { isFavorite = true },
                        isFavorite = isFavorite
                    )
                }
            }

            onNodeWithContentDescription(label = "favorite").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "unFavorite").assertDoesNotExist()
            onNodeWithContentDescription(label = "favorite").performClick()
            onNodeWithContentDescription(label = "favorite").assertIsNotDisplayed()
            onNodeWithContentDescription(label = "unFavorite").assertIsDisplayed()
        }
    }

    @Test
    fun showUnFavoriteButtonClickTest() {
        composeTestRule.apply {
            setContent {
                var isFavorite by remember { mutableStateOf(false) }

                if (isFavorite) {
                    FavoriteButtonComponent(
                        modifier = Modifier.clickable { isFavorite = false },
                        isFavorite = isFavorite
                    )
                } else {
                    FavoriteButtonComponent(
                        modifier = Modifier.clickable { isFavorite = true },
                        isFavorite = isFavorite
                    )
                }
            }

            onNodeWithContentDescription(label = "unFavorite").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "favorite").assertDoesNotExist()
            onNodeWithContentDescription(label = "unFavorite").performClick()
            onNodeWithContentDescription(label = "unFavorite").assertIsNotDisplayed()
            onNodeWithContentDescription(label = "favorite").assertIsDisplayed()
        }
    }
}