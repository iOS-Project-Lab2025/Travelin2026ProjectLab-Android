plugins {
    id("com.softserveacademy.android.feature")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.booking.flight.presentation"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.ui.test.junit4)
    implementation(projects.feature.booking.flight.domain)
    implementation(projects.feature.booking.common.domain)
    implementation(projects.core.presentation.designSystem)
    implementation(projects.feature.booking.common.presentation)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.glide.compose)
    implementation(libs.stripe.android)
    testImplementation(libs.bundles.testing.stack)
}
