plugins {
    id("com.softserveacademy.android.feature")
}

android {
    namespace = "com.softserveacademy.core.presentation.ui"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:presentation:design_system"))
    implementation(libs.androidx.appcompat)
}
