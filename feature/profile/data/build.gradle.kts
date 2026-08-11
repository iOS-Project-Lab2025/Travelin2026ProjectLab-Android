plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.profile.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.feature.profile.domain)
    implementation(projects.core.data)
    testImplementation(libs.bundles.testing.stack)
    testImplementation(libs.kotlinx.coroutines.test)
}
