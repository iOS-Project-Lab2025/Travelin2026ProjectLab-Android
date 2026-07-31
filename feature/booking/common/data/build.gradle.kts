import java.util.Properties

plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
    kotlin("plugin.serialization") version "2.2.10"
}

android {
    namespace = "com.softserveacademy.feature.booking.common.data"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val localProperties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { localProperties.load(it) }
        }

        val stripeSecret = localProperties.getProperty("STRIPE_SECRET_KEY")
            ?: project.findProperty("STRIPE_SECRET_KEY")?.toString()
            ?: ""
        val stripePublishable = localProperties.getProperty("STRIPE_PUBLISHABLE_KEY")
            ?: project.findProperty("STRIPE_PUBLISHABLE_KEY")?.toString()
            ?: ""

        buildConfigField("String", "STRIPE_SECRET_KEY", "\"$stripeSecret\"")
        buildConfigField("String", "STRIPE_PUBLISHABLE_KEY", "\"$stripePublishable\"")
    }
}

dependencies {
    implementation(projects.feature.booking.common.domain)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.converter.scalars)

    // Stripe Android SDK
    implementation(libs.stripe.android)
    implementation(libs.stripe.financial.connections)
}
