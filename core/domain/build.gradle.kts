plugins {
    id("com.softserveacademy.domain.module")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(projects.core.error)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.bundles.testing.stack)
}
