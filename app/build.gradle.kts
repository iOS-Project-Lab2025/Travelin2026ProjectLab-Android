plugins {
    id("com.softserveacademy.android.application")
    id("com.softserveacademy.android.hilt")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.softserveacademy.travelin2026projectlab"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.softserveacademy.travelin2026projectlab"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // modules
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.designSystem)
    implementation(project(":feature:auth:login:domain"))
    implementation(project(":feature:auth:login:data"))
    implementation(project(":feature:auth:login:presentation"))
    implementation(project(":feature:auth:register:domain"))
    implementation(project(":feature:auth:register:data"))
    implementation(project(":feature:auth:register:presentation"))
    implementation(project(":feature:auth:common:domain"))
    implementation(project(":feature:auth:common:data"))
    implementation(project(":feature:auth:common:presentation"))
}