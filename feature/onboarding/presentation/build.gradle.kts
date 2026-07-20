plugins {
    id("com.softserveacademy.android.feature")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.onboarding.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.feature.onboarding.domain)
    implementation(projects.core.presentation.designSystem)
    implementation(projects.core.presentation.ui)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)
}

