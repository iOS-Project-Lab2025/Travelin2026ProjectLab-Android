plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.favorites.tours.domain"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
