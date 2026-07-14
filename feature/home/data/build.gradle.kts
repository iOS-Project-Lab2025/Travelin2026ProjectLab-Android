plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.home.data"
}

dependencies {
    implementation(libs.androidx.ui.tooling.preview)
    implementation(projects.core.domain)
    implementation(projects.feature.home.domain)
    implementation(projects.core.data)
    testImplementation(libs.bundles.testing.stack)
    testImplementation(libs.kotlinx.coroutines.test)
    debugImplementation(libs.androidx.ui.tooling)
}