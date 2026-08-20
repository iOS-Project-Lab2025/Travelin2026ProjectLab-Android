plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.favorites.tours.data"
}

dependencies {
    implementation(projects.feature.favorites.tours.domain)
    implementation(projects.feature.favorites.common.domain)
    implementation(projects.core.domain)
    implementation(libs.javax.inject)
}
