plugins {
    id("com.softserveacademy.android.library")
}

android {
    namespace = "com.softserveacademy.feature.booking.data"
}

dependencies {
    implementation(projects.feature.booking.domain)
}
