plugins {
    alias(libs.plugins.bowoon.android.library)
    alias(libs.plugins.bowoon.hilt)
}

android {
    namespace = "com.bowoon.kurlypretest.core.data"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    arrayOf(
        projects.core.model,
        projects.core.database,
        libs.androidx.compose.paging,
        libs.threetenabp
    ).forEach {
        implementation(it)
    }

    api(projects.core.common)
    api(projects.core.network)

    testImplementation(projects.core.testing)
}