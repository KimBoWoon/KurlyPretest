package com.kurly.android.mockserver

import com.kurly.android.mockserver.core.FileProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
internal class MockServerTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @Inject
    lateinit var fileProvider: FileProvider

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun 리소스_파일_읽기_테스트() {
        (1 .. 4).forEach { page -> println(fileProvider.getJsonFromAsset("sections/sections_$page.json")) }
        (1 .. 20).forEach { page -> println(fileProvider.getJsonFromAsset("section/products/section_products_$page.json")) }
    }
}