plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.auth.login.data"
}

dependencies {
    implementation(projects.feature.auth.login.domain)
    implementation(projects.feature.auth.common.domain)
    implementation(projects.feature.auth.common.data)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(libs.androidx.datastore.preferences)
}
