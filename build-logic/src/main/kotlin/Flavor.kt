enum class FlavorDimension {
    contentType
}

enum class TravelinFlavor(
    val dimension: FlavorDimension,
    val applicationIdSuffix: String? = null,
    val baseUrl: String = "https://private-amnesiac-923781-travelin1.apiary-mock.com/"
) {
    dev(FlavorDimension.contentType, ".dev"),
    staging(FlavorDimension.contentType, ".staging"),
    prod(FlavorDimension.contentType)
}
