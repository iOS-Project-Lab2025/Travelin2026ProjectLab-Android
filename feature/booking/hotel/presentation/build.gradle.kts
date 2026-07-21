plugins {
    id("com.softserveacademy.android.feature")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.booking.hotel.presentation"
}

dependencies {
    implementation(projects.feature.booking.hotel.domain)
    implementation(projects.core.presentation.designSystem)
}
