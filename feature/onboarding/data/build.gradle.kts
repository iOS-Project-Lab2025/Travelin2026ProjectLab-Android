plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.onboarding.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.feature.onboarding.domain)
    implementation(projects.core.data)
}