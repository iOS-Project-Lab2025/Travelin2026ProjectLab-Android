plugins {
    id("com.softserveacademy.android.library")
}

android {
    namespace = "com.softserveacademy.feature.favorites.common.domain"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.serialization.json)
}
