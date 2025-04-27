package com.bowoon.main

import androidx.paging.testing.asSnapshot
import com.bowoon.model.MainSection
import com.bowoon.model.Product
import com.bowoon.model.Section
import com.bowoon.model.SectionType
import com.bowoon.testing.model.testSectionInfo
import com.bowoon.testing.repository.TestDatabaseRepository
import com.bowoon.testing.repository.TestSectionRepository
import com.bowoon.testing.utils.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class MainVMTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
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

        runBlocking {
            testDatabaseRepository.productDatabase.emit(emptyList())
        }
    }

    @Test
    fun sectionFlowTest() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.sectionPager.collect { println(it) } }

        testDatabaseRepository.insertProduct(product = Product(id = 1))
        testDatabaseRepository.insertProduct(product = Product(id = 5))
        testDatabaseRepository.insertProduct(product = Product(id = 6))

        assertEquals(
            viewModel.sectionPager.asSnapshot().take(5),
            testSectionInfo.data?.flatMap { section: Section ->
                listOf(
                    MainUiModel.Section(
                        section = MainSection(
                            sectionId = section.id,
                            type = SectionType.entries.find { it.label == section.type } ?: SectionType.NONE,
                            title = section.title,
                            products = section.products?.data?.map { product ->
                                if (product.id == 1 || product.id == 5 || product.id == 6) product.copy(isFavorite = true) else product
                            }
                        )
                    ),
                    MainUiModel.Separator
                )
            }?.dropLast(1) ?: emptyList()
        )
    }

    @Test
    fun addFavoriteTest() = runTest {
        val product = Product(
            id = 1,
            name = "productName1",
            image = "/productImageUrl1.png",
            originalPrice = 3000,
            discountedPrice = 2800,
            isSoldOut = false,
            isFavorite = false
        )

        assertEquals(
            testDatabaseRepository.getProducts().first(),
            emptyList()
        )

        viewModel.addFavorite(product)

        assertEquals(
            testDatabaseRepository.getProducts().first(),
            listOf(product)
        )
    }

    @Test
    fun removeFavoriteTest() = runTest {
        val product = Product(
            id = 1,
            name = "productName1",
            image = "/productImageUrl1.png",
            originalPrice = 3000,
            discountedPrice = 2800,
            isSoldOut = false,
            isFavorite = false
        )

        assertEquals(
            testDatabaseRepository.getProducts().first(),
            emptyList()
        )

        viewModel.addFavorite(product)

        assertEquals(
            testDatabaseRepository.getProducts().first(),
            listOf(product)
        )

        viewModel.removeFavorite(product)

        assertEquals(
            testDatabaseRepository.getProducts().first(),
            emptyList()
        )
    }
}