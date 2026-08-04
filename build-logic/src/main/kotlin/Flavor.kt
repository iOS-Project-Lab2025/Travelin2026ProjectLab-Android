enum class FlavorDimension {
    contentType
}

enum class TravelinFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    dev(FlavorDimension.contentType, ".dev"),
    staging(FlavorDimension.contentType, ".staging"),
    prod(FlavorDimension.contentType)
}
