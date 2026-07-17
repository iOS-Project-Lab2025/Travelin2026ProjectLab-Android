plugins {
    id("com.softserveacademy.android.feature")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.booking.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.feature.booking.domain)
    implementation(projects.core.presentation.designSystem)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}
