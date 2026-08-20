plugins {
    id("com.softserveacademy.android.feature")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.favorites.tours.presentation"
}

dependencies {
    implementation(projects.feature.favorites.tours.domain)
    implementation(projects.feature.favorites.common.domain)
    implementation(projects.feature.favorites.common.presentation)
    implementation(projects.core.presentation.designSystem)
    implementation(projects.core.domain)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.javax.inject)
}
