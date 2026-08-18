plugins {
    id("com.softserveacademy.android.feature")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.booking.tour.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designSystem)

    implementation(projects.feature.booking.tour.domain)
    implementation(projects.feature.booking.common.domain)
    implementation(projects.feature.booking.common.presentation)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.stripe.android)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}
