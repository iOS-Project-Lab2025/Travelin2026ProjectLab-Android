plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.softserveacademy.feature.auth.common.data"
}

dependencies {
    implementation(projects.feature.auth.common.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(libs.androidx.datastore.preferences)

    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.auth)
    implementation(libs.supabase.postgrest)

    testImplementation(libs.bundles.testing.stack)
}
