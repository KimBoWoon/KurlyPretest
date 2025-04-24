package com.bowoon.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.bowoon.model.Product
import com.bowoon.model.SectionType
import com.bowoon.ui.components.ProductComponent
import org.junit.Rule
import org.junit.Test

class ProductTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun verticalProductTest() {
        composeTestRule.apply {
            setContent {
                ProductComponent(
                    product = Product(
                        id = 1,
                        name = "productName1",
                        image = "/productImageUrl1.png",
                        originalPrice = 4900,
                        discountedPrice = 2700,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    type = SectionType.VERTICAL,
                    addFavorite = {},
                    removeFavorite = {}
                )
            }

            onNodeWithText(text = "productName1").assertExists().assertIsDisplayed()
            onNodeWithText(text = "4900원").assertExists().assertIsDisplayed()
            onNodeWithText(text = "2700원").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "unFavorite").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "/productImageUrl1.png").assertExists().assertIsDisplayed()
        }
    }

    @Test
    fun horizontalProductTest() {
        composeTestRule.apply {
            setContent {
                ProductComponent(
                    product = Product(
                        id = 1,
                        name = "productName1",
                        image = "/productImageUrl1.png",
                        originalPrice = 4900,
                        discountedPrice = 2700,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    type = SectionType.HORIZONTAL,
                    addFavorite = {},
                    removeFavorite = {}
                )
            }

            onNodeWithText(text = "productName1").assertExists().assertIsDisplayed()
            onNodeWithText(text = "4900원").assertExists().assertIsDisplayed()
            onNodeWithText(text = "2700원").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "unFavorite").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "/productImageUrl1.png").assertExists().assertIsDisplayed()
        }
    }

    @Test
    fun gridProductTest() {
        composeTestRule.apply {
            setContent {
                ProductComponent(
                    product = Product(
                        id = 1,
                        name = "productName1",
                        image = "/productImageUrl1.png",
                        originalPrice = 4900,
                        discountedPrice = 2700,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    type = SectionType.GRID,
                    addFavorite = {},
                    removeFavorite = {}
                )
            }

            onNodeWithText(text = "productName1").assertExists().assertIsDisplayed()
            onNodeWithText(text = "4900원").assertExists().assertIsDisplayed()
            onNodeWithText(text = "2700원").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "unFavorite").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "/productImageUrl1.png").assertExists().assertIsDisplayed()
        }
    }

    @Test
    fun addFavoriteProductTest() {
        composeTestRule.apply {
            setContent {
                var isFavorite by remember { mutableStateOf(false) }

                ProductComponent(
                    product = Product(
                        id = 1,
                        name = "productName1",
                        image = "/productImageUrl1.png",
                        originalPrice = 4900,
                        discountedPrice = 2700,
                        isSoldOut = false,
                        isFavorite = isFavorite
                    ),
                    type = SectionType.VERTICAL,
                    addFavorite = { isFavorite = true },
                    removeFavorite = { isFavorite = false }
                )
            }

            onNodeWithText(text = "productName1").assertExists().assertIsDisplayed()
            onNodeWithText(text = "4900원").assertExists().assertIsDisplayed()
            onNodeWithText(text = "2700원").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "/productImageUrl1.png").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "unFavorite").assertExists().assertIsDisplayed().performClick()
            onNodeWithContentDescription(label = "favorite").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "unFavorite").assertDoesNotExist()
        }
    }

    @Test
    fun removeFavoriteProductTest() {
        composeTestRule.apply {
            setContent {
                var isFavorite by remember { mutableStateOf(true) }

                ProductComponent(
                    product = Product(
                        id = 1,
                        name = "productName1",
                        image = "/productImageUrl1.png",
                        originalPrice = 4900,
                        discountedPrice = 2700,
                        isSoldOut = false,
                        isFavorite = isFavorite
                    ),
                    type = SectionType.VERTICAL,
                    addFavorite = { isFavorite = true },
                    removeFavorite = { isFavorite = false }
                )
            }

            onNodeWithText(text = "productName1").assertExists().assertIsDisplayed()
            onNodeWithText(text = "4900원").assertExists().assertIsDisplayed()
            onNodeWithText(text = "2700원").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "/productImageUrl1.png").assertExists().assertIsDisplayed()
            onNodeWithContentDescription(label = "unFavorite").assertDoesNotExist()
            onNodeWithContentDescription(label = "favorite").assertExists().assertIsDisplayed().performClick()
            onNodeWithContentDescription(label = "unFavorite").assertExists().assertIsDisplayed()
        }
    }
}