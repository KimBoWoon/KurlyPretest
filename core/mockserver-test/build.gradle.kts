plugins {
    alias(libs.plugins.bowoon.android.library)
    alias(libs.plugins.bowoon.hilt)
}

android {
    namespace = "com.bowoon.kurlypretest.core.mockserver_test"
}

dependencies {
    implementation(libs.hilt.android.testing)
    api(projects.core.mockserver)
}