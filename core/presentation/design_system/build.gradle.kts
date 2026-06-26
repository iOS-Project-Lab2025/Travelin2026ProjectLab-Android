plugins {
    id("com.softserveacademy.android.feature")
}

android {
    namespace = "com.softserveacademy.core.presentation.design_system"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(projects.core.domain)
}
