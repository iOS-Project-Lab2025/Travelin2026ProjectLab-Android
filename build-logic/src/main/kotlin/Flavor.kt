enum class FlavorDimension {
    contentType
}

enum class TravelinFlavor(
    val dimension: FlavorDimension,
    val applicationIdSuffix: String? = null,
    val baseUrl: String
) {
    dev(FlavorDimension.contentType, ".dev", "https://travelin2026projectlab-android-api.onrender.com/"),
    staging(FlavorDimension.contentType, ".staging", "https://travelin2026projectlab-android-api.onrender.com/"),
    prod(FlavorDimension.contentType, baseUrl = "https://travelin2026projectlab-android-api.onrender.com/")
}
