plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.softserveacademy.core.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.error)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.appcompat)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.google.places)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.koog.agents)
    implementation(libs.koog.google.client)
}
