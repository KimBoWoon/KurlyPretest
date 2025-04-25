package com.bowoon.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
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
    private lateinit var productPriceString: String
    private lateinit var productDiscountRate: String

    @Before
    fun setup() {
        testSectionRepository = TestSectionRepository()
        testDatabaseRepository = TestDatabaseRepository()
        viewModel = MainVM(
            sectionRepository = testSectionRepository,
            databaseRepository = testDatabaseRepository
        )

        composeTestRule.activity.apply {
            productPriceString = getString(com.bowoon.kurlypretest.core.ui.R.string.product_price)
            productDiscountRate = getString(com.bowoon.kurlypretest.core.ui.R.string.discount_rate)
        }
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

            viewModel.sectionPager.asSnapshot()
                .filter { it != MainUiModel.Separator }
                .filterIsInstance<MainUiModel.Section>()
                .forEach {
                    onNodeWithContentDescription(label = "sectionList")
                        .performScrollToNode(hasText(text = it.section.title!!))
                        .assertExists()
                        .assertIsDisplayed()

                    when (it.section.type) {
                        SectionType.NONE -> {}
                        SectionType.VERTICAL -> {
                            it.section.products?.forEach { product ->
                                onNodeWithContentDescription(label = "verticalSectionList")
                                    .performScrollToNode(hasText(text = product.name!!))
                                    .assertExists()
                                    .assertIsDisplayed()

                                product.discountedPrice?.let { discountedPrice ->
                                    onNodeWithText(text = productDiscountRate.format(getDiscountRate(product.originalPrice ?: 0, discountedPrice).toInt(), "%"))
                                }
                                onNodeWithContentDescription(label = "verticalSectionList")
                                    .performScrollToNode(hasText(text = productPriceString.format(product.originalPrice)))
                                    .assertExists()
                                    .assertIsDisplayed()
                                product.discountedPrice?.let { discountedPrice ->
                                    onNodeWithContentDescription(label = "verticalSectionList")
                                        .performScrollToNode(hasText(text = productPriceString.format(discountedPrice)))
                                        .assertExists()
                                        .assertIsDisplayed()
                                }
                                onNodeWithContentDescription(label = "verticalSectionList")
                                    .performScrollToNode(hasContentDescription(value = product.image!!))
                                    .assertExists()
                                    .assertIsDisplayed()
                            }
                        }

                        SectionType.HORIZONTAL -> {
                            it.section.products?.forEach { product ->
                                onNodeWithContentDescription(label = "horizontalSectionList")
                                    .performScrollToNode(hasText(text = product.name!!))
                                    .assertExists()
                                    .assertIsDisplayed()
                                product.discountedPrice?.let { discountedPrice ->
                                    onNodeWithText(text = productDiscountRate.format(getDiscountRate(product.originalPrice ?: 0, discountedPrice).toInt(), "%"))
                                }
                                onNodeWithContentDescription(label = "horizontalSectionList")
                                    .performScrollToNode(hasText(text = productPriceString.format(product.originalPrice)))
                                    .assertExists()
                                    .assertIsDisplayed()
                                product.discountedPrice?.let { discountedPrice ->
                                    onNodeWithContentDescription(label = "horizontalSectionList")
                                        .performScrollToNode(hasText(text = productPriceString.format(discountedPrice)))
                                        .assertExists()
                                        .assertIsDisplayed()
                                }
                                onNodeWithContentDescription(label = "horizontalSectionList")
                                    .performScrollToNode(hasContentDescription(value = product.image!!))
                                    .assertExists()
                                    .assertIsDisplayed()
                            }
                        }

                        SectionType.GRID -> {
                            it.section.products?.forEach { product ->
                                onNodeWithText(text = product.name!!)
                                    .assertExists()
                                    .assertIsDisplayed()
                                product.discountedPrice?.let { discountedPrice ->
                                    onNodeWithText(text = productDiscountRate.format(getDiscountRate(product.originalPrice ?: 0, discountedPrice).toInt(), "%"))
                                }
                                onNodeWithText(text = productPriceString.format(product.originalPrice))
                                    .assertExists()
                                    .assertIsDisplayed()
                                product.discountedPrice?.let { discountedPrice ->
                                    onNodeWithText(text = productPriceString.format(discountedPrice))
                                        .assertExists()
                                        .assertIsDisplayed()
                                }
                                onNodeWithContentDescription(label = product.image!!)
                                    .assertExists()
                                    .assertIsDisplayed()
                            }
                        }
                    }
                }
        }
    }

    @Test
    fun addFavoriteTest() = runTest {
        val products = listOfNotNull(
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

            products.forEach { product ->
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
                                    .performScrollToNode(hasContentDescription(value = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "unFavorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                                    .performClick()

                                onNodeWithContentDescription(label = "verticalSectionList")
                                    .performScrollToNode(hasContentDescription(value = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "${product.name}_productComponent"))
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
                                    .performScrollToNode(hasContentDescription(value = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "unFavorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                                    .performClick()

                                onNodeWithContentDescription(label = "horizontalSectionList")
                                    .performScrollToNode(hasContentDescription(value = "${product.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "${product.name}_productComponent"))
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

                                onNodeWithContentDescription(label = "${product.name}_productComponent")
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "unFavorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                                    .performClick()

                                onNodeWithContentDescription(label = "${product.name}_productComponent")
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "favorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                            }
                        }
                    }
            }

            val favoriteProducts = testDatabaseRepository.getProducts().first()
            products.forEach { product ->
                assertTrue(favoriteProducts.find { it.id == product.id } != null)
            }
        }
    }

    @Test
    fun removeFavoriteTest() = runTest {
        val favoriteProducts = listOfNotNull(
            testSectionInfo.data?.get(1)?.products?.data?.get(2),
            testSectionInfo.data?.get(1)?.products?.data?.get(5),
            testSectionInfo.data?.get(0)?.products?.data?.get(2),
            testSectionInfo.data?.get(2)?.products?.data?.get(1),
            testSectionInfo.data?.get(0)?.products?.data?.get(4),
            testSectionInfo.data?.get(2)?.products?.data?.get(4),
            testSectionInfo.data?.get(1)?.products?.data?.get(3)
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

            testDatabaseRepository.productDatabase.emit(favoriteProducts)

            favoriteProducts.forEach { favoriteProduct ->
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
                                    .performScrollToNode(hasContentDescription(value = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "favorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                                    .performClick()

                                onNodeWithContentDescription(label = "verticalSectionList")
                                    .performScrollToNode(hasContentDescription(value = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "${favoriteProduct.name}_productComponent"))
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
                                    .performScrollToNode(hasContentDescription(value = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "favorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                                    .performClick()

                                onNodeWithContentDescription(label = "horizontalSectionList")
                                    .performScrollToNode(hasContentDescription(value = "${favoriteProduct.name}_productComponent"))
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "${favoriteProduct.name}_productComponent"))
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

                                onNodeWithContentDescription(label = "${favoriteProduct.name}_productComponent")
                                    .onChildren()
                                    .filterToOne(hasContentDescription(value = "favorite"))
                                    .assertExists()
                                    .assertIsDisplayed()
                                    .performClick()

                                onNodeWithContentDescription(label = "${favoriteProduct.name}_productComponent")
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