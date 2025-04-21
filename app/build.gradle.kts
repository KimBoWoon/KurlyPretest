plugins {
    alias(libs.plugins.bowoon.android.application)
    alias(libs.plugins.bowoon.android.application.compose)
    alias(libs.plugins.bowoon.hilt)
    alias(libs.plugins.bowoon.android.application.flavors)
}

dependencies {
    arrayOf(
        projects.core.ui,
        projects.feature.main,
        libs.coil.compose,
        libs.threetenabp,
    ).forEach {
        implementation(it)
    }

    ksp(libs.hilt.compiler)
    ksp(libs.hilt.ext.compiler)
}