package com.softserveacademy.home.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.home.presentation.model.HotelInfoUi
import com.softserveacademy.home.presentation.model.TripDetailUi

/**
 * Scrollable content body for the upcoming trip detail screen.
 *
 * Displays the trip summary card, followed by each flight booking
 * section, and then the hotel booking section if present.
 *
 * @param tripDetail The full trip detail data to render.
 * @param modifier   Optional modifier applied to the root column.
 */
@Composable
fun TravelTripDetailContent(
    tripDetail: TripDetailUi,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = TravelinDimens.PaddingMedium)
    ) {
        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        TravelTripSummaryCard(tripDetail = tripDetail)

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

        tripDetail.flightBookings.forEach { booking ->
            TravelFlightBookingSection(booking = booking)
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
        }

        tripDetail.hotelInfo?.let { hotel: HotelInfoUi ->
            TravelHotelBookingSection(hotel = hotel)
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
        }
    }
}
