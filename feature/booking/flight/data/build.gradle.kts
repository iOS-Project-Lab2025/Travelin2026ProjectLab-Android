plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.softserveacademy.feature.booking.flight.data"

}

dependencies {
    implementation(projects.feature.booking.flight.domain)
    implementation(libs.kotlinx.serialization.json) // Necesary to parse JSON
    implementation(libs.javax.inject) // To Hilt recognize @Inject
    implementation(libs.retrofit.core)
    implementation(libs.androidx.datastore.preferences)
}
