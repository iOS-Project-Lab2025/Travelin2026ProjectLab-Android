plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.auth.common.data"
}

dependencies {
    implementation(projects.feature.auth.common.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(libs.androidx.datastore.preferences)
}
