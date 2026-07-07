plugins {
    id("com.softserveacademy.android.feature")
}

android {
    namespace = "com.softserveacademy.home.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designSystem)
}