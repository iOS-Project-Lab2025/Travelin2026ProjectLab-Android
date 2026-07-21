plugins {
    id("com.softserveacademy.android.feature")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.booking.hotel.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designSystem)

    implementation(projects.feature.booking.hotel.domain)
    implementation(projects.feature.booking.common.domain)
    implementation(projects.feature.booking.common.presentation)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}
