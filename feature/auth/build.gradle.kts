plugins {
    id("com.softserveacademy.android.feature")
}

android {
    namespace = "com.softserveacademy.feature.auth"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:presentation:design_system"))
    
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
