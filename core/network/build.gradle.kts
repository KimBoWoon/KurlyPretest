plugins {
    alias(libs.plugins.bowoon.android.library)
    alias(libs.plugins.bowoon.hilt)
}

android {
    namespace = "com.bowoon.kurlypretest.core.network"
}

dependencies {
    arrayOf(
        projects.core.common,
        projects.core.model,
        projects.core.mockserver,
        libs.kotlinx.serialization.converter,
        libs.kotlinx.serialization.json,
        libs.retrofit2,
        libs.okhttp.okhttp,
        libs.okhttp.profiler,
        libs.okhttp.logging,
        libs.threetenabp,
        platform(libs.okhttp.bom)
    ).forEach {
        implementation(it)
    }

    testImplementation(projects.core.testing)
}