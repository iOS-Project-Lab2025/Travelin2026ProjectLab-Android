import java.util.Properties

plugins {
    id("com.softserveacademy.android.feature")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.home.presentation"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val localProperties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { localProperties.load(it) }
        }

        val mapsApiKey = localProperties.getProperty("MAPS_API_KEY")
            ?: project.findProperty("MAPS_API_KEY")?.toString()
            ?: ""

        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
        buildConfigField("String", "MAPS_API_KEY", "\"$mapsApiKey\"")
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.feature.profile.domain)
    implementation(projects.feature.profile.presentation)
    implementation(projects.feature.home.domain)
    implementation(projects.feature.favorites.common.domain)
    implementation(projects.core.presentation.designSystem)
    implementation(projects.core.presentation.ui)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(projects.feature.auth.common.domain)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    testImplementation(libs.bundles.testing.stack)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    // Maps SDK for Android
    implementation(libs.maps.compose)
}
