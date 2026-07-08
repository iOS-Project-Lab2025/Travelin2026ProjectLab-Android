plugins {
    id("com.softserveacademy.android.feature")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.profile.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.feature.profile.domain)
    implementation(projects.core.presentation.designSystem)
    implementation(projects.core.presentation.ui)
    implementation(projects.feature.auth.common.domain)
}
