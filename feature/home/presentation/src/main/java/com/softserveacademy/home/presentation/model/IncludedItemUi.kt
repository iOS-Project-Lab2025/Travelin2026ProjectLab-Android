package com.softserveacademy.home.presentation.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.softserveacademy.core.presentation.design_system.theme.AcUnitIcon
import com.softserveacademy.core.presentation.design_system.theme.BreakfastIcon
import com.softserveacademy.core.presentation.design_system.theme.CleaningServicesIcon
import com.softserveacademy.core.presentation.design_system.theme.FitnessCenterIcon
import com.softserveacademy.core.presentation.design_system.theme.ParkingIcon
import com.softserveacademy.core.presentation.design_system.theme.PoolIcon
import com.softserveacademy.core.presentation.design_system.theme.RoomServiceIcon
import com.softserveacademy.core.presentation.design_system.theme.WifiIcon
import com.softserveacademy.home.presentation.R

/**
 * UI model representing an item included in the hotel or travel package.
 *
 * @property title The string resource ID for the name of the included service or amenity.
 * @property icon The ImageVector icon representing this item.
 * @property subtitle Optional string resource ID for additional information about the item.
 */
data class IncludedItemUi(
    @StringRes val title: Int,
    val icon: ImageVector,
    @StringRes val subtitle: Int? = null
) {
    companion object {
        val BuffetBreakfast: IncludedItemUi
            @Composable get() = IncludedItemUi(
                title = R.string.buffet_breakfast_label,
                icon = BreakfastIcon
            )
        val FreeWifi: IncludedItemUi
            @Composable get() = IncludedItemUi(
                title = R.string.free_wifi_label,
                icon = WifiIcon,
            )
        val FitnessCenter: IncludedItemUi
            @Composable get() = IncludedItemUi(
                title = R.string.fitness_center_label,
                icon = FitnessCenterIcon,
            )
        val Pool: IncludedItemUi
            @Composable get() = IncludedItemUi(
                title = R.string.pool_label,
                icon = PoolIcon,
            )
        val CleaningServices: IncludedItemUi
            @Composable get() = IncludedItemUi(
                title = R.string.cleaning_services_label,
                icon = CleaningServicesIcon,
            )
        val SelfParking: IncludedItemUi
            @Composable get() = IncludedItemUi(
                title = R.string.parking_label,
                icon = ParkingIcon
            )
        val RoomService: IncludedItemUi
            @Composable get() = IncludedItemUi(
                title = R.string.room_service_label,
                icon = RoomServiceIcon,
            )
        val AcUnit: IncludedItemUi
            @Composable get() = IncludedItemUi(
                title = R.string.ac_unit_label,
                icon = AcUnitIcon,
            )
    }
}
