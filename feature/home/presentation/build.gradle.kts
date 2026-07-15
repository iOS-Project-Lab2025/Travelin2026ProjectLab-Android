plugins {
    id("com.softserveacademy.android.feature")
}

android {
    namespace = "com.softserveacademy.home.presentation"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.feature.home.domain)
    implementation(projects.core.presentation.designSystem)

    // testing toolkit
    testImplementation(libs.bundles.testing.stack)

    // Necessary to Compose "draw" on tests
    testImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

