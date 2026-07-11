plugins {
    id("com.softserveacademy.android.feature")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.home.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.feature.home.domain)
    implementation(projects.core.presentation.designSystem)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(projects.feature.auth.common.domain)

    testImplementation(libs.bundles.testing.stack)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}