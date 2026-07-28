plugins {
    id("com.softserveacademy.domain.module")
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.kotlinx.coroutines.core)
    
    testImplementation(libs.bundles.testing.stack)
}
