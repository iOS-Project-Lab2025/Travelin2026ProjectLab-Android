plugins {
    id("com.softserveacademy.domain.module")
}

dependencies {
    implementation(project(":feature:auth:common:domain"))
    implementation(projects.core.domain)
    implementation(libs.kotlinx.coroutines.core)
}
