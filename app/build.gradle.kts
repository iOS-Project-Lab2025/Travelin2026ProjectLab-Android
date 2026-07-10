plugins {
    id("com.softserveacademy.android.application")
    id("com.softserveacademy.android.hilt")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
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
    //Implementation needed for navgraph
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)


    // modules
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.designSystem)
    implementation(projects.feature.auth.login.domain)
    implementation(projects.feature.auth.login.data)
    implementation(projects.feature.auth.login.presentation)
    implementation(projects.feature.auth.register.domain)
    implementation(projects.feature.auth.register.data)
    implementation(projects.feature.auth.register.presentation)
    implementation(projects.feature.auth.common.domain)
    implementation(projects.feature.auth.common.data)
    implementation(projects.feature.auth.common.presentation)
    implementation(projects.feature.home.domain)
    implementation(projects.feature.home.data)
    implementation(projects.feature.home.presentation)
}