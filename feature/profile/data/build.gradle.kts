plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.softserveacademy.profile.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.feature.profile.domain)
    implementation(projects.core.data)

    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.auth)
    implementation(libs.supabase.postgrest)

    testImplementation(libs.bundles.testing.stack)
    testImplementation(libs.kotlinx.coroutines.test)
}
