package com.softserveacademy.core.presentation.design_system.components.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.softserveacademy.core.domain.model.HotelRoomAmenity
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.theme.AcUnitIcon
import com.softserveacademy.core.presentation.design_system.theme.BreakfastIcon
import com.softserveacademy.core.presentation.design_system.theme.FitnessCenterIcon
import com.softserveacademy.core.presentation.design_system.theme.ParkingIcon
import com.softserveacademy.core.presentation.design_system.theme.PoolIcon
import com.softserveacademy.core.presentation.design_system.theme.RoomServiceIcon
import com.softserveacademy.core.presentation.design_system.theme.WifiIcon

/**
 * Extension property to get the [ImageVector] icon associated with a [HotelRoomAmenity].
 */
val HotelRoomAmenity.icon: ImageVector
    @Composable
    get() = when (this) {
        HotelRoomAmenity.WIFI -> WifiIcon
        HotelRoomAmenity.BREAKFAST -> BreakfastIcon
        HotelRoomAmenity.PARKING -> ParkingIcon
        HotelRoomAmenity.POOL -> PoolIcon
        HotelRoomAmenity.GYM -> FitnessCenterIcon
        HotelRoomAmenity.AC -> AcUnitIcon
        HotelRoomAmenity.ROOM_SERVICE -> RoomServiceIcon
    }

/**
 * Extension property to get the display name associated with a [HotelRoomAmenity].
 */
val HotelRoomAmenity.displayName: String
    @Composable
    get() = when (this) {
        HotelRoomAmenity.WIFI -> stringResource(id = R.string.amenity_wifi)
        HotelRoomAmenity.BREAKFAST -> stringResource(id = R.string.amenity_breakfast)
        HotelRoomAmenity.PARKING -> stringResource(id = R.string.amenity_parking)
        HotelRoomAmenity.POOL -> stringResource(id = R.string.amenity_pool)
        HotelRoomAmenity.GYM -> stringResource(id = R.string.amenity_gym)
        HotelRoomAmenity.AC -> stringResource(id = R.string.amenity_ac)
        HotelRoomAmenity.ROOM_SERVICE -> stringResource(id = R.string.amenity_room_service)
    }
