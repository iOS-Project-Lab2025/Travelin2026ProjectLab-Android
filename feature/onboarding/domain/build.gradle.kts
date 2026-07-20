plugins {
    id("com.softserveacademy.domain.module")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.javax.inject)

}
