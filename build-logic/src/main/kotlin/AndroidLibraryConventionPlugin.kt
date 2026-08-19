import com.android.build.api.dsl.LibraryExtension
import java.util.Properties
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")

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

            extensions.configure<LibraryExtension> {
                compileSdk = libs.findVersion("compileSdk").get().requiredVersion.toInt()
                defaultConfig {
                    minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                    val mapsApiKey = getProperty("MAPS_API_KEY")
                        ?: project.findProperty("MAPS_API_KEY")?.toString()
                        ?: ""
                    manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
                    buildConfigField("String", "MAPS_API_KEY", "\"$mapsApiKey\"")

                    val supabaseUrl = getProperty("SUPABASE_URL")
                        ?: project.findProperty("SUPABASE_URL")?.toString()
                        ?: ""
                    buildConfigField("String", "SUPABASE_URL", "\"$supabaseUrl\"")

                    val supabaseKey = getProperty("SUPABASE_KEY")
                        ?: project.findProperty("SUPABASE_KEY")?.toString()
                        ?: ""
                    buildConfigField("String", "SUPABASE_KEY", "\"$supabaseKey\"")
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
                buildFeatures {
                    buildConfig = true
                }
                flavorDimensions += FlavorDimension.contentType.name
                productFlavors {
                    TravelinFlavor.entries.forEach { flavor ->
                        create(flavor.name) {
                            dimension = flavor.dimension.name
                            buildConfigField(
                                "String",
                                "BASE_URL",
                                "\"${getProperty("BASE_URL") ?: flavor.baseUrl}\""
                            )
                        }
                    }
                }
            }

            tasks.withType(KotlinCompile::class.java).configureEach {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx-core-ktx").get())
                add("testImplementation", libs.findLibrary("junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx-espresso-core").get())
            }
        }
    }
}
