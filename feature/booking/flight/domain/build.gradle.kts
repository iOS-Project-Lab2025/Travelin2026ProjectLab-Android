plugins {
    id("com.softserveacademy.domain.module")
    id("org.jetbrains.kotlin.plugin.serialization")
}

dependencies {
    api(projects.core.domain)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.serialization.json)
}
