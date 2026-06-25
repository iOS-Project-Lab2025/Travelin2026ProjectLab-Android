plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.core.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.appcompat)
}
