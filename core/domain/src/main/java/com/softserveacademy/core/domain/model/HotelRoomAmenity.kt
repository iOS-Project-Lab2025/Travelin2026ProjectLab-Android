package com.softserveacademy.core.domain.model

import kotlinx.serialization.Serializable

/**
 * Enum class representing the amenities available in a hotel room.
*/
@Serializable
enum class HotelRoomAmenity {
    WIFI,
    BREAKFAST,
    PARKING,
    POOL,
    GYM,
    AC,
    ROOM_SERVICE
}
