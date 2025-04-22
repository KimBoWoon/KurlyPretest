plugins {
    alias(libs.plugins.bowoon.android.library)
    alias(libs.plugins.bowoon.android.room)
    alias(libs.plugins.bowoon.hilt)
}

android {
    namespace = "com.bowoon.kurlypretest.core.database"
}

dependencies {
    arrayOf(
        projects.core.common,
        libs.kotlinx.serialization.json,
        libs.androidx.room.runtime,
        libs.androidx.room.ktx,
        libs.threetenabp
    ).forEach {
        implementation(it)
    }

    api(projects.core.model)

    arrayOf(
        libs.androidx.room.compiler
    ).forEach {
        ksp(it)
    }
}