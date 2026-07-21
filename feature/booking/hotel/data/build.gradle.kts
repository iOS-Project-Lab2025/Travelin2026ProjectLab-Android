plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.booking.hotel.data"
}

dependencies {
    implementation(projects.feature.booking.hotel.domain)
}
