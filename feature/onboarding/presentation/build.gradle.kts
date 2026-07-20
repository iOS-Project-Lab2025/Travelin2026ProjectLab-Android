plugins {
    id("com.softserveacademy.android.feature")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.onboarding.presentation"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.androidx.ui.test.junit4)
    implementation(projects.core.domain)
    implementation(projects.feature.onboarding.domain)
    implementation(projects.core.presentation.designSystem)
    implementation(projects.core.presentation.ui)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    //unit tests
    testImplementation(libs.bundles.testing.stack)

    // UI Tests
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}



