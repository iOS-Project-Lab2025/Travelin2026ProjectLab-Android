plugins {
    id("com.softserveacademy.android.feature")

}

android {
    namespace = "com.softserveacademy.core.presentation.design_system"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // testing toolkit
    testImplementation(libs.bundles.testing.stack)

    // Necessary to Compose "draw" on tests
    testImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}


