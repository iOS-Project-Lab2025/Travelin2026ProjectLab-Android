plugins {
    id("com.softserveacademy.android.library")
}

android {
    namespace = "com.softserveacademy.core.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.androidx.appcompat)
}
