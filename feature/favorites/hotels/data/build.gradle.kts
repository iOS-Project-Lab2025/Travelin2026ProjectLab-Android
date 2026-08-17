plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.favorites.hotels.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.feature.favorites.hotels.domain)
    implementation(projects.feature.favorites.common.domain)
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.javax.inject)
    
    testImplementation(libs.bundles.testing.stack)
    testImplementation(libs.kotlinx.coroutines.test)
}
