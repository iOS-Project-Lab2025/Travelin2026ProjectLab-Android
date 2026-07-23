plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.booking.flight.data"
}

dependencies {
    implementation(projects.feature.booking.flight.domain)
}
