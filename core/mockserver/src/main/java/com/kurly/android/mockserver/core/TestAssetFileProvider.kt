package com.kurly.android.mockserver.core

import javax.inject.Inject

class TestAssetFileProvider @Inject constructor() : FileProvider {
    override fun getJsonFromAsset(filePath: String): String? {
        val inputStream = javaClass.classLoader?.getResourceAsStream(filePath) ?: return null
        return inputStream.bufferedReader().use { it.readText() }
    }
}
