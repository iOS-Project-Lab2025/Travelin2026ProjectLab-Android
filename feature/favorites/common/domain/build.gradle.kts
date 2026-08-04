plugins {
    id("com.softserveacademy.android.library")
}

android {
    namespace = "com.softserveacademy.feature.favorites.common.data"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.domain)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
}
