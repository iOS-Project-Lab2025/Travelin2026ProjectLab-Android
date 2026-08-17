plugins {
    id("com.softserveacademy.domain.module")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)

    testImplementation(libs.bundles.testing.stack)
}
