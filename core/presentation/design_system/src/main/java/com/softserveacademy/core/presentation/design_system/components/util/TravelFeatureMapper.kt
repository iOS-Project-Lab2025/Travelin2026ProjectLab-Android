package com.softserveacademy.core.presentation.design_system.components.util

import com.softserveacademy.core.presentation.design_system.model.FeatureUi
import com.softserveacademy.core.presentation.design_system.R

/**
 * Maps a string ID to a [FeatureUi] containing localized labels and icons.
 *
 * @param value The string ID representing the amenity or service (e.g., "HOTEL_WIFI", "TOUR_BREAKFAST").
 * @return The corresponding [FeatureUi], or null if not found.
 */
fun mapToFeature(value: String): FeatureUi? {
    return when (value) {
        // -----------------------------------------------------------------------------------------
        // Hotel Features (Amenities)
        // -----------------------------------------------------------------------------------------
        "HOTEL_WIFI" -> FeatureUi(
            iconRes = R.drawable.ic_wifi,
            labelRes = R.string.feat_hotel_free_wifi
        )

        "HOTEL_ROOM_SERVICE" -> FeatureUi(
            iconRes = R.drawable.ic_room_service,
            labelRes = R.string.feat_hotel_room_service
        )

        "HOTEL_CLEANING_SERVICES" -> FeatureUi(
            iconRes = R.drawable.ic_cleaning_services,
            labelRes = R.string.feat_hotel_cleaning_services
        )

        "HOTEL_PARKING" -> FeatureUi(
            iconRes = R.drawable.ic_parking_sign,
            labelRes = R.string.feat_hotel_parking
        )

        "HOTEL_AC" -> FeatureUi(
            iconRes = R.drawable.ic_ac_unit,
            labelRes = R.string.feat_hotel_ac_unit
        )

        "HOTEL_FITNESS_CENTER" -> FeatureUi(
            iconRes = R.drawable.ic_fitness_center,
            labelRes = R.string.feat_hotel_fitness_center
        )

        "HOTEL_SPA" -> FeatureUi(
            iconRes = R.drawable.ic_spa,
            labelRes = R.string.feat_hotel_spa
        )

        "HOTEL_POOL" -> FeatureUi(
            iconRes = R.drawable.ic_pool,
            labelRes = R.string.feat_hotel_pool
        )

        "HOTEL_RESTAURANT" -> FeatureUi(
            iconRes = R.drawable.ic_restaurant,
            labelRes = R.string.feat_hotel_restaurant
        )

        "HOTEL_BAR" -> FeatureUi(
            iconRes = R.drawable.ic_drink,
            labelRes = R.string.feat_hotel_bar_and_lounge
        )

        "HOTEL_BUFFET_BREAKFAST" -> FeatureUi(
            iconRes = R.drawable.ic_breakfast,
            labelRes = R.string.feat_hotel_buffet_breakfast
        )

        "HOTEL_BREAKFAST" -> FeatureUi(
            iconRes = R.drawable.ic_breakfast,
            labelRes = R.string.feat_hotel_breakfast
        )

        // -----------------------------------------------------------------------------------------
        // Tour Services
        // -----------------------------------------------------------------------------------------
        "TOUR_TRANSPORT" -> FeatureUi(
            iconRes = R.drawable.ic_bus,
            labelRes = R.string.feat_tour_transport
        )
        "TOUR_BUS"-> FeatureUi(
            iconRes = R.drawable.ic_bus,
            labelRes = R.string.feat_tour_bus
        )
        "TOUR_CAR"-> FeatureUi(
            iconRes = R.drawable.ic_car,
            labelRes = R.string.feat_tour_car
        )
        "TOUR_BREAKFAST" -> FeatureUi(
            iconRes = R.drawable.ic_breakfast,
            labelRes = R.string.feat_tour_breakfast
        )
        "TOUR_LAUNCH" -> FeatureUi(
            iconRes = R.drawable.ic_restaurant,
            labelRes = R.string.feat_tour_launch
        )
        "TOUR_DINNER" -> FeatureUi(
            iconRes = R.drawable.ic_restaurant,
            labelRes = R.string.feat_tour_dinner
        )
        "TOUR_SNACK" -> FeatureUi(
            iconRes = R.drawable.ic_snack,
            labelRes = R.string.feat_tour_dinner
        )
        "TOUR_GUIDE" -> FeatureUi(
            iconRes = R.drawable.ic_guide,
            labelRes = R.string.feat_tour_guide
        )
        "TOUR_AUDIO_GUIDE" -> FeatureUi(
            iconRes = R.drawable.ic_headphones,
            labelRes = R.string.feat_tour_audio_guide
        )
        "TOUR_LOCAL_GUIDE" -> FeatureUi(
            iconRes = R.drawable.ic_guide,
            labelRes = R.string.feat_tour_local_guide
        )
        "TOUR_PROFESSIONAL_GUIDE" -> FeatureUi(
            iconRes = R.drawable.ic_guide,
            labelRes = R.string.feat_tour_professional_guide
        )
        "TOUR_TRANSLATION" -> FeatureUi(
            iconRes = R.drawable.ic_translation,
            labelRes = R.string.feat_tour_translation
        )
        "TOUR_TICKET" -> FeatureUi(
            iconRes = R.drawable.ic_ticket,
            labelRes = R.string.feat_tour_ticket
        )
        "TOUR_PARK_TICKET" -> FeatureUi(
            iconRes = R.drawable.ic_ticket,
            labelRes = R.string.feat_tour_park_ticket
        )
        "TOUR_SHOW_TICKET" -> FeatureUi(
            iconRes = R.drawable.ic_ticket,
            labelRes = R.string.feat_tour_show_ticket
        )
        "TOUR_SAFETY_EQUIPMENT" -> FeatureUi(
            iconRes = R.drawable.ic_helmet,
            labelRes = R.string.feat_tour_safety_equipment
        )
        "TOUR_EQUIPMENT_RENTAL" -> FeatureUi(
            iconRes = R.drawable.ic_helmet,
            labelRes = R.string.feat_tour_equipment_rental
        )

        else -> null
    }
}
