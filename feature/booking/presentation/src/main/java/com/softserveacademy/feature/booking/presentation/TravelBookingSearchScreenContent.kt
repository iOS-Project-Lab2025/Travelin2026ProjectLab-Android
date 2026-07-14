package com.softserveacademy.feature.booking.presentation

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
import com.softserveacademy.feature.booking.presentation.components.TravelDateRangePicker
import com.softserveacademy.feature.booking.presentation.components.TravelGuestBottomSheet
import androidx.compose.foundation.layout.fillMaxSize
import java.util.Calendar
import java.util.TimeZone

/**
 * A generic composable that represents the content of a booking search screen.
 * Decoupled from specific booking types (e.g., Hotel, Flight).
 *
 * @param state The state of the booking search screen.
 * @param onEvent Callback when a UI event occurs.
 * @param modifier The modifier to be applied to the content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelBookingSearchScreenContent(
    state: TravelBookingSearchState,
    onEvent: (TravelBookingSearchEvent) -> Unit,
    modifier: Modifier = Modifier
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
                TravelBookingSearchEvent.OnDateRangeSelected(
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
                    onBackClick = { onEvent(TravelBookingSearchEvent.OnBackClick) },
                    onNextClick = { onEvent(TravelBookingSearchEvent.OnNextClick) }
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            modifier = modifier
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                InlineErrorBanner(
                    message = state.dateErrorMessage?.let { stringResource(it) } ?: "",
                    isVisible = state.isDateErrorVisible,
                    modifier = Modifier.padding(TravelinDimens.PaddingMedium)
                )
                TravelDateRangePicker(
                    title = stringResource(R.string.booking_date_picker_title),
                    state = dateRangePickerState
                )
            }
        }
    }

    if (state.showGuestBottomSheet) {
        TravelGuestBottomSheet(
            adults = state.adultsCount,
            kids = state.childrenCount,
            hasPets = state.hasPets,
            onAdultsChange = { onEvent(TravelBookingSearchEvent.OnAdultsCountChange(it)) },
            onKidsChange = { onEvent(TravelBookingSearchEvent.OnChildrenCountChange(it)) },
            onHasPetsChange = { onEvent(TravelBookingSearchEvent.OnHasPetsChange(it)) },
            onAccept = { onEvent(TravelBookingSearchEvent.OnAcceptGuests) },
            onDismissRequest = { onEvent(TravelBookingSearchEvent.OnDismissGuestBottomSheet) },
            isErrorVisible = state.isGuestErrorVisible,
            errorMessage = state.guestErrorMessage?.let { stringResource(it) }
        )
    }
}

@Preview(name = "Select Dates")
@Composable
private fun TravelBookingSearchScreenContentPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        TravelBookingSearchScreenContent(
            state = TravelBookingSearchState(),
            onEvent = {}
        )
    }
}

@Preview(name = "Guest Bottom Sheet")
@Composable
private fun TravelBookingGuestBottomSheetPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        TravelBookingSearchScreenContent(
            state = TravelBookingSearchState(showGuestBottomSheet = true),
            onEvent = {}
        )
    }
}
