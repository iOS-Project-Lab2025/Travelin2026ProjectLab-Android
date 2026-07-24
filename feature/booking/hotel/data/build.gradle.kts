plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
    kotlin("plugin.serialization") version "2.2.10"
}

android {
    namespace = "com.softserveacademy.feature.booking.hotel.data"
}

dependencies {
    implementation(projects.feature.booking.hotel.domain)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
}
