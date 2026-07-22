plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
    kotlin("plugin.serialization") version "2.2.10"
}

android {
    namespace = "com.softserveacademy.feature.booking.common.data"
}

dependencies {
    implementation(projects.feature.booking.common.domain)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
}
