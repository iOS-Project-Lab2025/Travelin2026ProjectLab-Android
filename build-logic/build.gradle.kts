plugins {
    `kotlin-dsl`
}

group = "com.softserveacademy.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.compose.gradlePlugin)
    implementation(libs.hilt.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "com.softserveacademy.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "com.softserveacademy.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "com.softserveacademy.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("domainModule") {
            id = "com.softserveacademy.domain.module"
            implementationClass = "DomainModuleConventionPlugin"
        }
        register("androidHilt") {
            id = "com.softserveacademy.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
    }
}
