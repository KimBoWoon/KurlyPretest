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
import kotlinx.coroutines.flow.collect
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
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.sectionPager.collect() }

        testDatabaseRepository.insertProduct(product = Product(id = 1))
        testDatabaseRepository.insertProduct(product = Product(id = 5))
        testDatabaseRepository.insertProduct(product = Product(id = 6))

        val list = viewModel.sectionPager.asSnapshot()

        assertEquals(
            list.filter { it != MainUiModel.Separator }.take(3),
            testSectionInfo.data?.map<Section, MainUiModel> { section ->
                MainUiModel.Section(
                    section = MainSection(
                        sectionId = section.id,
                        type = SectionType.entries.find { section.type == it.label } ?: SectionType.NONE,
                        title = section.title,
                        products = testSectionInfo.data?.get(section.id ?: 0)?.products?.data?.map { product ->
                            if (product.id == 1 || product.id == 5 || product.id == 6) product.copy(isFavorite = true) else product
                        }
                    )
                )
            }
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