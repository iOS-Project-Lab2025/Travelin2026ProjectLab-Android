enum class FlavorDimension {
    contentType
}

enum class TravelinFlavor(
    val dimension: FlavorDimension,
    val applicationIdSuffix: String? = null,
    val baseUrl: String
) {
    dev(
        FlavorDimension.contentType,
        ".dev",
        "https://private-amnesiac-923781-travelin1.apiary-mock.com/"
    ),
    staging(
        FlavorDimension.contentType,
        ".staging",
        "https://private-amnesiac-923781-travelin1.apiary-mock.com/"
    ),
    prod(
        FlavorDimension.contentType,
        null,
        "https://private-amnesiac-923781-travelin1.apiary-mock.com/"
    )
}
