import java.util.Properties

plugins {
    id("com.softserveacademy.android.feature")

}

android {
    namespace = "com.softserveacademy.core.presentation.design_system"

    defaultConfig {
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use {
                localProperties.load(it)
            }
        }
        
        val mapsKey = (localProperties.getProperty("MAPS_API_KEY") 
            ?: project.findProperty("MAPS_API_KEY") 
            ?: System.getenv("MAPS_API_KEY") 
            ?: "YOUR_API_KEY").toString()
            
        manifestPlaceholders["MAPS_API_KEY"] = mapsKey
        buildConfigField("String", "MAPS_API_KEY", "\"$mapsKey\"")
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.androidx.activity.compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // testing toolkit
    testImplementation(libs.bundles.testing.stack)

    // Necessary to Compose "draw" on tests
    testImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Maps SDK for Android
    implementation(libs.maps.compose)
}


