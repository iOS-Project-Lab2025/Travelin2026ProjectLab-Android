plugins {
    id("com.softserveacademy.domain.module")
    kotlin("plugin.serialization") version "2.2.10"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
}
