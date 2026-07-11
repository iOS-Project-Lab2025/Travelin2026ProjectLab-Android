plugins {
    id("com.softserveacademy.domain.module")
}
dependencies{
    implementation(projects.core.domain)
    testImplementation(libs.bundles.testing.stack)
    testImplementation(libs.kotlinx.coroutines.test)
}