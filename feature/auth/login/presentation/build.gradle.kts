plugins {
    id("com.softserveacademy.android.feature")
}

android {
    namespace = "com.softserveacademy.feature.auth.login.presentation"
}

dependencies {
    implementation(projects.feature.auth.login.domain)
    implementation(projects.feature.auth.common.domain)
    implementation(projects.feature.auth.common.presentation)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designSystem)
    
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
