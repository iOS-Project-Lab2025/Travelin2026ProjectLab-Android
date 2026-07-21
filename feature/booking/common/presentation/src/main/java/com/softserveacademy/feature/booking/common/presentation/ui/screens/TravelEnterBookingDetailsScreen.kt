package com.softserveacademy.feature.booking.common.presentation.ui.screens

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.InlineErrorBanner
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.common.presentation.events.TravelEnterBookingDetailsEvent
import com.softserveacademy.feature.booking.common.presentation.states.TravelEnterBookingDetailsState
import com.softserveacademy.feature.booking.common.presentation.ui.components.TravelBookingBottomBar
import com.softserveacademy.feature.booking.common.presentation.ui.components.TravelBookingDateRangePicker
import com.softserveacademy.feature.booking.common.presentation.ui.components.TravelBookingCountBottomSheet
import com.softserveacademy.feature.booking.common.presentation.ui.components.util.TravelBookingCountItem
import com.softserveacademy.feature.booking.common.presentation.R
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
    val todayStartUtc = remember {
        Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = state.startDateMillis,
        initialSelectedEndDateMillis = state.endDateMillis,
        yearRange = IntRange(currentYear, currentYear + 3),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayStartUtc
            }
        }
    )

    // Sync state to picker state and correct past dates when state changes (e.g. after async load from DataStore)
    LaunchedEffect(state.startDateMillis, state.endDateMillis) {
        var correctedStart = state.startDateMillis
        var correctedEnd = state.endDateMillis
        var updated = false

        if (correctedStart != null && correctedStart < todayStartUtc) {
            correctedStart = todayStartUtc
            updated = true
        }
        if (correctedEnd != null && correctedEnd < todayStartUtc) {
            correctedEnd = todayStartUtc
            updated = true
        }

        if (updated) {
            onEvent(
                TravelEnterBookingDetailsEvent.OnDateRangeSelected(
                    correctedStart,
                    correctedEnd
                )
            )
        }

        if (correctedStart != dateRangePickerState.selectedStartDateMillis ||
            correctedEnd != dateRangePickerState.selectedEndDateMillis
        ) {
            dateRangePickerState.setSelection(correctedStart, correctedEnd)
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
