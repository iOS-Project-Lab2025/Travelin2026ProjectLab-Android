plugins {
    id("com.softserveacademy.android.feature")
}

android {
    namespace = "com.softserveacademy.feature.booking.presentation"
}

dependencies {
    implementation(projects.feature.booking.domain)
    implementation(projects.core.presentation.designSystem)
}
