plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
    kotlin("plugin.serialization") version "2.2.10"
}

android {
    namespace = "com.softserveacademy.feature.favorites.common.data"
}

dependencies {
    implementation(projects.feature.favorites.common.domain)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
}