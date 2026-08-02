package com.softserveacademy.core.domain.model

import java.io.Serializable

/**
 * Enum class representing the amenities available in a hotel room.
*/
enum class HotelRoomAmenity : Serializable {
    WIFI,
    BREAKFAST,
    PARKING,
    POOL,
    GYM,
    AC,
    ROOM_SERVICE
}
