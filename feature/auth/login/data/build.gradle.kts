plugins {
    id("com.softserveacademy.android.library")
    id("com.softserveacademy.android.hilt")
}

android {
    namespace = "com.softserveacademy.feature.auth.login.data"
}

dependencies {
    implementation(project(":feature:auth:login:domain"))
    implementation(project(":feature:auth:common:domain"))
    implementation(project(":feature:auth:common:data"))
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(libs.androidx.datastore.preferences)
}
