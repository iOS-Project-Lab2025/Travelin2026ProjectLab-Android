plugins {
    id("com.softserveacademy.android.feature") // Incluye Compose y dependencias UI
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.favorites.common.hotel.presentation"
}

dependencies {
    implementation(projects.core.presentation.designSystem)
    implementation(projects.core.domain)
    implementation(projects.feature.favorites.common.domain)
    implementation(projects.feature.favorites.hotels.domain)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
    debugImplementation(libs.androidx.compose.ui.tooling)
}