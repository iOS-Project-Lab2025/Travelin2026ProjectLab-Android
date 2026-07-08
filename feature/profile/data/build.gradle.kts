plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.profile.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.feature.profile.domain)
    implementation(projects.core.data)
}
