plugins {
    alias(libs.plugins.bowoon.android.library)
    alias(libs.plugins.bowoon.hilt)
}

android {
    namespace = "com.bowoon.kurlypretest.core.mockserver"

    defaultConfig {
        testInstrumentationRunner = "com.bowoon.testing.TestKurlyApplication"
    }

    packaging {
        resources {
            excludes.add("/META-INF/versions/9/OSGI-INF/MANIFEST.MF")
        }
    }
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.11.0")
    androidTestImplementation(libs.robolectric)
    androidTestImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
    androidTestImplementation(projects.core.testing)
}