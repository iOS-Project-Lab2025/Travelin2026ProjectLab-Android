includeBuild("build-logic")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Travelin2026ProjectLab"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:data")
include(":core:domain")
include(":core:presentation:design_system")
include(":core:presentation:ui")
include(":feature:auth:login:domain")
include(":feature:auth:login:data")
include(":feature:auth:login:presentation")
include(":feature:auth:register:domain")
include(":feature:auth:register:data")
include(":feature:auth:register:presentation")
include(":feature:auth:common:domain")
include(":feature:auth:common:data")
include(":feature:auth:common:presentation")
include(":feature:home:data")
include(":feature:home:domain")
include(":feature:home:presentation")
