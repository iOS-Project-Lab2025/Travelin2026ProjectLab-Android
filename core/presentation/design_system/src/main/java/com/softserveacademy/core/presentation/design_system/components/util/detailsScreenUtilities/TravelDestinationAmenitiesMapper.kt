package com.softserveacademy.core.presentation.design_system.components.util.detailsScreenUtilities

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.softserveacademy.core.domain.model.Amenities
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.theme.AcUnitIcon
import com.softserveacademy.core.presentation.design_system.theme.BreakfastIcon
import com.softserveacademy.core.presentation.design_system.theme.CleaningServicesIcon
import com.softserveacademy.core.presentation.design_system.theme.FitnessCenterIcon
import com.softserveacademy.core.presentation.design_system.theme.ParkingIcon
import com.softserveacademy.core.presentation.design_system.theme.PoolIcon
import com.softserveacademy.core.presentation.design_system.theme.RoomServiceIcon
import com.softserveacademy.core.presentation.design_system.theme.WifiIcon

/**
 * Extension property to get the display the icon associated with a [Amenities].
 */

val Amenities.icon: ImageVector
    @Composable
    get() = when (this) {
        Amenities.BuffetBreakfast -> BreakfastIcon
        Amenities.FreeWifi -> WifiIcon
        Amenities.FitnessCenter -> FitnessCenterIcon
        Amenities.Pool -> PoolIcon
        Amenities.CleaningServices -> CleaningServicesIcon
        Amenities.SelfParking -> ParkingIcon
        Amenities.RoomService -> RoomServiceIcon
        Amenities.AcUnit -> AcUnitIcon
    }

/**
 * Extension property to get the display name associated with a [Amenities].
 */
val Amenities.title: String
    @Composable
    get() = when (this) {
        Amenities.BuffetBreakfast -> stringResource(id = R.string.buffet_breakfast_label)
        Amenities.FreeWifi ->stringResource(id = R.string.free_wifi_label)
        Amenities.FitnessCenter -> stringResource(id = R.string.fitness_center_label)
        Amenities.Pool -> stringResource(id = R.string.pool_label)
        Amenities.CleaningServices -> stringResource(id = R.string.cleaning_services_label)
        Amenities.SelfParking -> stringResource(id = R.string.parking_label)
        Amenities.RoomService -> stringResource(id = R.string.room_service_label)
        Amenities.AcUnit -> stringResource(id = R.string.ac_unit_label)
    }