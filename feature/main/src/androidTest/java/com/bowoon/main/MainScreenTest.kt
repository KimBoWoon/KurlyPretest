package com.bowoon.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.testing.asSnapshot
import com.bowoon.common.getDiscountRate
import com.bowoon.model.SectionType
import com.bowoon.testing.model.testSectionInfo
import com.bowoon.testing.repository.TestDatabaseRepository
import com.bowoon.testing.repository.TestSectionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var viewModel: MainVM
    private lateinit var testSectionRepository: TestSectionRepository
    private lateinit var testDatabaseRepository: TestDatabaseRepository

    @Before
    fun setup() {
        testSectionRepository = TestSectionRepository()
        testDatabaseRepository = TestDatabaseRepository()
        viewModel = MainVM(
            sectionRepository = testSectionRepository,
            databaseRepository = testDatabaseRepository
        )
    }

    @Test
    fun productListTest() = runTest {
        composeTestRule.apply {
            setContent {
                MainScreen(
                    sectionPager = viewModel.sectionPager.collectAsLazyPagingItems(),
                    addFavorite = {},
                    removeFavorite = {}
                )
            }

            onNodeWithContentDescription(label = "mainLoadingProgress").assertExists().assertIsDisplayed()

            testDatabaseRepository.productDatabase.emit(emptyList())

            val items = viewModel.sectionPager.asSnapshot().filter { it != MainUiModel.Separator }.filterIsInstance<MainUiModel.Section>()
            items.forEach {
                onNodeWithContentDescription(label = "sectionList").performScrollToNode(hasText(text = it.section.title!!)).assertExists().assertIsDisplayed()

                when (it.section.type) {
                    SectionType.NONE -> {}
                    SectionType.VERTICAL -> {
                        it.section.products?.forEach { product ->
                            onNodeWithContentDescription(label = "verticalSectionList").performScrollToNode(hasText(text = product.name!!)).assertExists().assertIsDisplayed()
                            product.discountedPrice?.let { discountedPrice ->
                                onNodeWithText(text = "${getDiscountRate(product.originalPrice ?: 0, discountedPrice)}%")
                            }
                            onNodeWithContentDescription(label = "verticalSectionList").performScrollToNode(hasText(text = "${product.originalPrice}원")).assertExists().assertIsDisplayed()
                            product.discountedPrice?.let { discountedPrice ->
                                onNodeWithContentDescription(label = "verticalSectionList").performScrollToNode(hasText(text = "${discountedPrice}원")).assertExists().assertIsDisplayed()
                            }
                            onNodeWithContentDescription(label = "verticalSectionList").performScrollToNode(hasContentDescription(value = product.image!!)).assertExists().assertIsDisplayed()
                        }
                    }
                    SectionType.HORIZONTAL -> {
                        it.section.products?.forEach { product ->
                            onNodeWithContentDescription(label = "horizontalSectionList").performScrollToNode(hasText(text = product.name!!)).assertExists().assertIsDisplayed()
                            product.discountedPrice?.let { discountedPrice ->
                                onNodeWithText(text = "${getDiscountRate(product.originalPrice ?: 0, discountedPrice)}%")
                            }
                            onNodeWithContentDescription(label = "horizontalSectionList").performScrollToNode(hasText(text = "${product.originalPrice}원")).assertExists().assertIsDisplayed()
                            product.discountedPrice?.let { discountedPrice ->
                                onNodeWithContentDescription(label = "horizontalSectionList").performScrollToNode(hasText(text = "${discountedPrice}원")).assertExists().assertIsDisplayed()
                            }
                            onNodeWithContentDescription(label = "horizontalSectionList").performScrollToNode(hasContentDescription(value = product.image!!)).assertExists().assertIsDisplayed()
                        }
                    }
                    SectionType.GRID -> {
                        it.section.products?.forEach { product ->
                            onNodeWithText(text = product.name!!).assertExists().assertIsDisplayed()
                            product.discountedPrice?.let { discountedPrice ->
                                onNodeWithText(text = "${getDiscountRate(product.originalPrice ?: 0, discountedPrice)}%")
                            }
                            onNodeWithText(text = "${product.originalPrice}원").assertExists().assertIsDisplayed()
                            product.discountedPrice?.let { discountedPrice ->
                                onNodeWithText(text = "${discountedPrice}원").assertExists().assertIsDisplayed()
                            }
                            onNodeWithContentDescription(label = product.image!!).assertExists().assertIsDisplayed()
                        }
                    }
                }
            }
        }
    }

    @Test
    fun addFavoriteTest() = runTest {
        val products = listOf(
            testSectionInfo.data?.get(1)?.products?.data?.get(3),
            testSectionInfo.data?.get(0)?.products?.data?.get(4),
            testSectionInfo.data?.get(2)?.products?.data?.get(2)
        )
        composeTestRule.apply {
            setContent {
                MainScreen(
                    sectionPager = viewModel.sectionPager.collectAsLazyPagingItems(),
                    addFavorite = viewModel::addFavorite,
                    removeFavorite = viewModel::removeFavorite
                )
            }

            onNodeWithContentDescription(label = "mainLoadingProgress").assertExists().assertIsDisplayed()

            testDatabaseRepository.productDatabase.emit(emptyList())

            products.filterNotNull().forEach { product ->
                viewModel.sectionPager.asSnapshot()
                    .filterIsInstance<MainUiModel.Section>()
                    .find { section -> section.section.products?.find { it.id == product.id } != null }
                    ?.section
                    ?.let { section ->
                        when (section.type) {
                            SectionType.NONE -> throw RuntimeException("Section type is none...")
                            SectionType.VERTICAL -> {
                                onNodeWithContentDescription(label = "sectionList")
                                    .performScrollToNode(hasContentDescription(value = "verticalSectionList"))
                                    .assertExists()
                                    .assertIsDisplayed()

                                onNodeWithContentDescription(label = "verticalSectionList")
                                    .performScrollToNode(hasTestTag(testTag = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasTestTag(testTag = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "unFavorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                                    .performClick()

                                onNodeWithContentDescription(label = "verticalSectionList")
                                    .performScrollToNode(hasTestTag(testTag = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasTestTag(testTag = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "favorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                            }
                            SectionType.HORIZONTAL -> {
                                onNodeWithContentDescription(label = "sectionList")
                                    .performScrollToNode(hasContentDescription(value = "horizontalSectionList"))
                                    .assertExists()
                                    .assertIsDisplayed()

                                onNodeWithContentDescription(label = "horizontalSectionList")
                                    .performScrollToNode(hasTestTag(testTag = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasTestTag(testTag = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "unFavorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                                    .performClick()

                                onNodeWithContentDescription(label = "horizontalSectionList")
                                    .performScrollToNode(hasTestTag(testTag = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasTestTag(testTag = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "favorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                            }
                            SectionType.GRID -> {
                                onNodeWithContentDescription(label = "sectionList")
                                    .performScrollToNode(hasContentDescription(value = "gridSectionList"))
                                    .assertExists()
                                    .assertIsDisplayed()

                                onNodeWithContentDescription(label = "gridSectionList")
                                    .performScrollToNode(hasTestTag(testTag = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasTestTag(testTag = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "unFavorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                                    .performClick()

                                onNodeWithContentDescription(label = "gridSectionList")
                                    .performScrollToNode(hasTestTag(testTag = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasTestTag(testTag = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "favorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                            }
                        }
                    }
            }

            val favoriteProducts = testDatabaseRepository.getProducts().first()
            products.filterNotNull().forEach { product ->
                assertTrue(favoriteProducts.find { it.id == product.id } != null)
            }
        }
    }

    @Test
    fun removeFavoriteTest() = runTest {
        val favoriteProducts = listOf(
//            testSectionInfo.data?.get(1)?.products?.data?.get(2),
//            testSectionInfo.data?.get(1)?.products?.data?.get(5),
            testSectionInfo.data?.get(0)?.products?.data?.get(2),
            testSectionInfo.data?.get(2)?.products?.data?.get(1),
            testSectionInfo.data?.get(0)?.products?.data?.get(4),
            testSectionInfo.data?.get(2)?.products?.data?.get(4),
//            testSectionInfo.data?.get(1)?.products?.data?.get(3)
        )
        composeTestRule.apply {
            setContent {
                MainScreen(
                    sectionPager = viewModel.sectionPager.collectAsLazyPagingItems(),
                    addFavorite = viewModel::addFavorite,
                    removeFavorite = viewModel::removeFavorite
                )
            }

            onNodeWithContentDescription(label = "mainLoadingProgress").assertExists().assertIsDisplayed()

            testDatabaseRepository.productDatabase.emit(favoriteProducts.filterNotNull())

            favoriteProducts.filterNotNull().forEach { favoriteProduct ->
                viewModel.sectionPager.asSnapshot()
                    .filterIsInstance<MainUiModel.Section>()
                    .find { section -> section.section.products?.find { product -> product.id == favoriteProduct.id } != null }
                    ?.section
                    ?.let { section ->
                        when (section.type) {
                            SectionType.NONE -> throw RuntimeException("Section type is none...")
                            SectionType.VERTICAL -> {
                                onNodeWithContentDescription(label = "sectionList")
                                    .performScrollToNode(hasContentDescription(value = "verticalSectionList"))
                                    .assertExists()
                                    .assertIsDisplayed()

                                onNodeWithContentDescription(label = "verticalSectionList")
                                    .performScrollToNode(hasTestTag(testTag = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasTestTag(testTag = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "favorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                                    .performClick()

                                onNodeWithContentDescription(label = "verticalSectionList")
                                    .performScrollToNode(hasTestTag(testTag = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasTestTag(testTag = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "unFavorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                            }
                            SectionType.HORIZONTAL -> {
                                onNodeWithContentDescription(label = "sectionList")
                                    .performScrollToNode(hasContentDescription(value = "horizontalSectionList"))
                                    .assertExists()
                                    .assertIsDisplayed()

                                onNodeWithContentDescription(label = "horizontalSectionList")
                                    .performScrollToNode(hasTestTag(testTag = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasTestTag(testTag = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "favorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                                    .performClick()

                                onNodeWithContentDescription(label = "horizontalSectionList")
                                    .performScrollToNode(hasTestTag(testTag = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasTestTag(testTag = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "unFavorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                            }
                            SectionType.GRID -> {
                                onNodeWithContentDescription(label = "sectionList")
                                    .performScrollToNode(hasContentDescription(value = "gridSectionList"))
                                    .assertExists()
                                    .assertIsDisplayed()

                                onNodeWithContentDescription(label = "gridSectionList")
                                    .performScrollToNode(hasTestTag(testTag = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasTestTag(testTag = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "favorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                                    .performClick()

                                onNodeWithContentDescription(label = "gridSectionList")
                                    .performScrollToNode(hasTestTag(testTag = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasTestTag(testTag = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "unFavorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                            }
                        }
                    }
            }

            assertEquals(
                testDatabaseRepository.getProducts().first(),
                emptyList()
            )
        }
    }
}