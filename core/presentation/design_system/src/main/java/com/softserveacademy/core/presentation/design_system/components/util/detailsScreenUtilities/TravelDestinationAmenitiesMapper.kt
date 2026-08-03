package com.softserveacademy.core.presentation.design_system.components.util.detailsScreenUtilities

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.softserveacademy.core.domain.model.IncludedItems
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
 * Extension property to get the display the icon associated with a [IncludedItems].
 */

val IncludedItems.icon: ImageVector
    @Composable
    get() = when (this) {
        IncludedItems.BuffetBreakfast -> BreakfastIcon
        IncludedItems.FreeWifi -> WifiIcon
        IncludedItems.FitnessCenter -> FitnessCenterIcon
        IncludedItems.Pool -> PoolIcon
        IncludedItems.CleaningServices -> CleaningServicesIcon
        IncludedItems.SelfParking -> ParkingIcon
        IncludedItems.RoomService -> RoomServiceIcon
        IncludedItems.AcUnit -> AcUnitIcon
    }

/**
 * Extension property to get the display name associated with a [IncludedItems].
 */
val IncludedItems.title: String
    @Composable
    get() = when (this) {
        IncludedItems.BuffetBreakfast -> stringResource(id = R.string.buffet_breakfast_label)
        IncludedItems.FreeWifi ->stringResource(id = R.string.free_wifi_label)
        IncludedItems.FitnessCenter -> stringResource(id = R.string.fitness_center_label)
        IncludedItems.Pool -> stringResource(id = R.string.pool_label)
        IncludedItems.CleaningServices -> stringResource(id = R.string.cleaning_services_label)
        IncludedItems.SelfParking -> stringResource(id = R.string.parking_label)
        IncludedItems.RoomService -> stringResource(id = R.string.room_service_label)
        IncludedItems.AcUnit -> stringResource(id = R.string.ac_unit_label)
    }