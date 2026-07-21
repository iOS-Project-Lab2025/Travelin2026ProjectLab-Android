package com.softserveacademy.feature.booking.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.InlineErrorBanner
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.presentation.components.TravelBookingBottomBar
import com.softserveacademy.feature.booking.presentation.components.TravelBookingLoadingScreen
import com.softserveacademy.feature.booking.presentation.components.TravelBookingDateRangePicker
import com.softserveacademy.feature.booking.presentation.components.TravelBookingCountBottomSheet
import com.softserveacademy.feature.booking.presentation.components.util.TravelBookingCountItem
import androidx.compose.foundation.layout.fillMaxSize
import java.util.Calendar
import java.util.TimeZone

/**
 * A stateless composable that represents the content of an enter booking details screen.
 * Allows the user to select a date range and the number of guests/passangers for the booking.
 *
 * Decoupled from specific booking types (e.g., Hotel, Flight) to reuse in different booking flows.
 *
 * @param state The state of the enter booking details screen.
 * @param onEvent Callback when a UI event occurs.
 * @param modifier The modifier to be applied to the content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TravelEnterBookingDetailsScreen(
    modifier: Modifier = Modifier,
    state: TravelEnterBookingDetailsState,
    bookingCountItems: List<TravelBookingCountItem> = emptyList(),
    onEvent: (TravelEnterBookingDetailsEvent) -> Unit,
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = state.startDateMillis,
        initialSelectedEndDateMillis = state.endDateMillis,
        yearRange = IntRange(currentYear, currentYear + 3),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val todayStartUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
                return utcTimeMillis >= todayStartUtc
            }
        }
    )

    // Sync state to picker state when state changes (e.g. after async load from DataStore)
    LaunchedEffect(state.startDateMillis, state.endDateMillis) {
        if (state.startDateMillis != dateRangePickerState.selectedStartDateMillis ||
            state.endDateMillis != dateRangePickerState.selectedEndDateMillis
        ) {
            dateRangePickerState.setSelection(state.startDateMillis, state.endDateMillis)
        }
    }

    LaunchedEffect(dateRangePickerState.selectedStartDateMillis, dateRangePickerState.selectedEndDateMillis) {
        if (dateRangePickerState.selectedStartDateMillis != state.startDateMillis ||
            dateRangePickerState.selectedEndDateMillis != state.endDateMillis
        ) {
            onEvent(
                TravelEnterBookingDetailsEvent.OnDateRangeSelected(
                    dateRangePickerState.selectedStartDateMillis,
                    dateRangePickerState.selectedEndDateMillis
                )
            )
        }
    }

    if (state.isLoading) {
        TravelBookingLoadingScreen()
    } else {
        Scaffold(
            bottomBar = {
                TravelBookingBottomBar(
                    onBackClick = { onEvent(TravelEnterBookingDetailsEvent.OnBackClick) },
                    onNextClick = { onEvent(TravelEnterBookingDetailsEvent.OnNextClick) }
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                InlineErrorBanner(
                    message = state.dateErrorMessage?.let { stringResource(it) } ?: "",
                    isVisible = state.isDateErrorVisible,
                    modifier = Modifier.padding(TravelinDimens.PaddingMedium)
                )
                TravelBookingDateRangePicker(
                    title = stringResource(R.string.booking_date_picker_title),
                    state = dateRangePickerState
                )
            }
        }
    }

    if (state.showGuestBottomSheet) {
        TravelBookingCountBottomSheet(
            items = bookingCountItems,
            onAccept = { onEvent(TravelEnterBookingDetailsEvent.OnAcceptClick) },
            onDismissRequest = { onEvent(TravelEnterBookingDetailsEvent.OnDismissBottomSheet) },
            isErrorVisible = state.isGuestErrorVisible,
            errorMessage = state.guestErrorMessage?.let { stringResource(it) }
        )
    }
}

@Preview(name = "Select Dates")
@Composable
private fun TravelEnterBookingDetailsScreenPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        TravelEnterBookingDetailsScreen(
            state = TravelEnterBookingDetailsState(),
            onEvent = {}
        )
    }
}

@Preview(name = "Count Bottom Sheet")
@Composable
private fun TravelEnterBookingDetailsBottomSheetPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        TravelEnterBookingDetailsScreen(
            state = TravelEnterBookingDetailsState(showGuestBottomSheet = true),
            onEvent = {}
        )
    }
}
