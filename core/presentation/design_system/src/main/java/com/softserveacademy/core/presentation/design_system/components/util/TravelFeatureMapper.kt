package com.softserveacademy.core.presentation.design_system.components.util

import com.softserveacademy.core.presentation.design_system.model.FeatureUi
import com.softserveacademy.core.presentation.design_system.R

/**
 * Maps a string ID to a [FeatureUi] containing localized labels and icons.
 *
 * @param value The string ID representing the amenity or service (e.g., "hotel.wifi").
 * @return The corresponding [FeatureUi], or null if not found.
 */
fun mapToFeature(value: String): FeatureUi? {
    return when (value) {
        "hotel.wifi", "FreeWifi", "WIFI" -> FeatureUi(
            iconRes = R.drawable.ic_wifi,
            labelRes = R.string.free_wifi_label
        )

        "hotel.breakfast", "BuffetBreakfast", "BREAKFAST" -> FeatureUi(
            iconRes = R.drawable.ic_breakfast,
            labelRes = R.string.buffet_breakfast_label
        )

        "hotel.room_service", "RoomService", "ROOM_SERVICE" -> FeatureUi(
            iconRes = R.drawable.ic_room_service,
            labelRes = R.string.room_service_label
        )

        "hotel.fitness_center", "FitnessCenter", "GYM" -> FeatureUi(
            iconRes = R.drawable.ic_fitness_center,
            labelRes = R.string.fitness_center_label
        )

        "hotel.pool", "Pool", "POOL" -> FeatureUi(
            iconRes = R.drawable.ic_pool,
            labelRes = R.string.pool_label
        )

        "hotel.cleaning_services", "CleaningServices" -> FeatureUi(
            iconRes = R.drawable.ic_cleaning_services,
            labelRes = R.string.cleaning_services_label
        )

        "hotel.parking", "SelfParking", "PARKING" -> FeatureUi(
            iconRes = R.drawable.ic_parking_sign,
            labelRes = R.string.parking_label
        )

        "hotel.ac", "AcUnit", "AC" -> FeatureUi(
            iconRes = R.drawable.ic_ac_unit,
            labelRes = R.string.ac_unit_label
        )

        "tour.transport" -> FeatureUi(
            iconRes = R.drawable.ic_bus,
            labelRes = R.string.retry_label // Placeholder as I don't have tour strings yet
        )

        else -> null
    }
}
