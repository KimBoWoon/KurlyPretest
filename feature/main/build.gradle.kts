plugins {
    alias(libs.plugins.bowoon.android.library)
    alias(libs.plugins.bowoon.android.library.compose)
    alias(libs.plugins.bowoon.android.feature)
}

android {
    namespace = "com.bowoon.kurlypretest.feature.main"
}

dependencies {
    arrayOf(
        projects.core.common,
        projects.core.data,
        projects.core.model,
        projects.core.ui,
        libs.coil.compose,
        libs.androidx.compose.paging,
        libs.threetenabp
    ).forEach {
        implementation(it)
    }

    testImplementation(projects.core.testing)
    testImplementation(libs.androidx.paging.testing)
    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.androidx.paging.testing)
}