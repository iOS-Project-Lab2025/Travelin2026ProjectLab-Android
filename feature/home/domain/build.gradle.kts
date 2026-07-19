plugins {
    id("com.softserveacademy.domain.module")
}
dependencies{
    implementation(projects.core.domain)
    implementation(libs.javax.inject)
    testImplementation(libs.bundles.testing.stack)
    testImplementation(libs.kotlinx.coroutines.test)
}