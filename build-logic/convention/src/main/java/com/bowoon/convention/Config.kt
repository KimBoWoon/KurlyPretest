package com.bowoon.convention

object Config {
    object ApplicationSetting {
        const val COMPILE_SDK_VERSION = 35
        const val MIN_SDK_VERSION = 24
        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val defaultProguardFile = "proguard-android-optimize.txt"
        const val proguardFile = "proguard-rules.pro"
        const val dateFormat = "HHmmss"
    }

    object Library {
        const val MIN_SDK_VERSION = 24
        const val COMPILE_SDK_VERSION = 35
    }

    sealed class Application(
        val appName: String,
        val applicationId: String,
        val compileSdkVersion: Int = ApplicationSetting.COMPILE_SDK_VERSION,
        val minSdkVersion: Int = ApplicationSetting.MIN_SDK_VERSION,
        val targetSdkVersion: Int = ApplicationSetting.COMPILE_SDK_VERSION,
        val versionCode: Int,
        val versionName: String,
    ) {
        object KurlyPretest : Application(
            appName = "kurlypretest",
            applicationId = "com.bowoon.kurlypretest",
            compileSdkVersion = ApplicationSetting.COMPILE_SDK_VERSION,
            minSdkVersion = ApplicationSetting.MIN_SDK_VERSION,
            targetSdkVersion = ApplicationSetting.COMPILE_SDK_VERSION,
            versionCode = 1,
            versionName = "1.0.0"
        )
    }
}
