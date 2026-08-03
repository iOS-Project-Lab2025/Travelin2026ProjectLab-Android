package com.softserveacademy.feature.booking.flight.presentation.ui.mappers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.softserveacademy.core.domain.model.CabinClass
import com.softserveacademy.core.presentation.design_system.theme.*
import com.softserveacademy.feature.booking.flight.domain.usecase.ValidateFlightSearchUseCase
import com.softserveacademy.feature.booking.flight.presentation.R

@Composable
fun CabinClass.toIcon(): ImageVector {
    return when (this) {
        CabinClass.ECONOMY -> EconomyClassIcon
        CabinClass.PREMIUM_ECONOMY -> PremiumEconomyClassIcon
        CabinClass.BUSINESS -> BusinessClassIcon
        CabinClass.FIRST -> FirstClassIcon
    }
}

@Composable
fun CabinClass.toDisplayName(): String {
    return when (this) {
        CabinClass.ECONOMY -> stringResource(R.string.flight_cabin_economy)
        CabinClass.PREMIUM_ECONOMY -> stringResource(R.string.flight_cabin_premium_economy)
        CabinClass.BUSINESS -> stringResource(R.string.flight_cabin_business)
        CabinClass.FIRST -> stringResource(R.string.flight_cabin_first)
    }
}

@Composable
fun ValidateFlightSearchUseCase.FlightError.toMessage(): String {
    val id = when (this) {
        ValidateFlightSearchUseCase.FlightError.INVALID_ORIGIN -> R.string.flight_error_invalid_origin
        ValidateFlightSearchUseCase.FlightError.INVALID_DESTINATION -> R.string.flight_error_invalid_destination
        ValidateFlightSearchUseCase.FlightError.SAME_LOCATION -> R.string.flight_error_same_location
        ValidateFlightSearchUseCase.FlightError.INVALID_DATE -> R.string.flight_error_invalid_date
        ValidateFlightSearchUseCase.FlightError.MISSING_RETURN_DATE -> R.string.flight_error_missing_return_date
        ValidateFlightSearchUseCase.FlightError.INVALID_DATE_SEQUENCE -> R.string.flight_error_date_sequence
    }
    return stringResource(id)
}