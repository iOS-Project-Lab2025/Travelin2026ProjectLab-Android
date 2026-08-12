import com.android.build.api.dsl.ApplicationExtension
import java.util.Properties
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            val localProperties = Properties()
            val localPropertiesFile = rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                localPropertiesFile.inputStream().use {
                    localProperties.load(it)
                }
            }

            fun getProperty(key: String): String? {
                return localProperties.getProperty(key) ?: System.getenv(key)
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = libs.findVersion("compileSdk").get().requiredVersion.toInt()
                defaultConfig {
                    minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
                    targetSdk = libs.findVersion("targetSdk").get().requiredVersion.toInt()
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    
                    val mapsApiKey = getProperty("MAPS_API_KEY")
                        ?: project.findProperty("MAPS_API_KEY")?.toString()
                        ?: ""
                    manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
                    buildConfigField("String", "MAPS_API_KEY", "\"$mapsApiKey\"")
                }

                val keystorePath = getProperty("KEYSTORE_PATH")
                val isReleaseSigningConfigured = keystorePath != null &&
                        getProperty("KEYSTORE_PASSWORD") != null &&
                        getProperty("KEY_ALIAS") != null &&
                        getProperty("KEY_PASSWORD") != null &&
                        rootProject.file(keystorePath).exists()

                signingConfigs {
                    if (isReleaseSigningConfigured) {
                        create("release") {
                            storeFile = file(getProperty("KEYSTORE_PATH")!!)
                            storePassword = getProperty("KEYSTORE_PASSWORD")!!
                            keyAlias = getProperty("KEY_ALIAS")!!
                            keyPassword = getProperty("KEY_PASSWORD")!!
                        }
                    }
                }

                buildTypes {
                    debug {
                        applicationIdSuffix = ".debug"
                    }
                    release {
                        isMinifyEnabled = true
                        signingConfig = if (isReleaseSigningConfigured) {
                            signingConfigs.getByName("release")
                        } else {
                            signingConfigs.getByName("debug")
                        }
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
                flavorDimensions += FlavorDimension.contentType.name
                productFlavors {
                    TravelinFlavor.entries.forEach { flavor ->
                        create(flavor.name) {
                            dimension = flavor.dimension.name
                            if (flavor.applicationIdSuffix != null) {
                                applicationIdSuffix = flavor.applicationIdSuffix
                            }
                            buildConfigField(
                                "String",
                                "BASE_URL",
                                "\"${flavor.baseUrl}\""
                            )
                        }
                    }
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
                buildFeatures {
                    compose = true
                    buildConfig = true
                }
            }

            tasks.withType(KotlinCompile::class.java).configureEach {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }
        }
    }
}
