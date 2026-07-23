plugins {
    id("com.softserveacademy.android.feature")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.booking.flight.presentation"
}

dependencies {
    implementation(projects.feature.booking.flight.domain)
    implementation(projects.core.presentation.designSystem)
}
