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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.presentation.design_system.components.InlineErrorBanner
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.presentation.components.TravelDateRangePicker
import com.softserveacademy.feature.booking.presentation.components.TravelGuestBottomSheet
import com.softserveacademy.feature.booking.presentation.components.TravelHotelBookingBottomBar
import java.util.Calendar
import java.util.TimeZone

/**
 * Composable that represents the hotel booking search screen.
 *
 * @param modifier The modifier to be applied to the screen.
 * @param onBackClick The callback to be invoked when the back button is clicked.
 * @param viewModel The view model for the hotel booking search screen.
 */
@Composable
fun HotelBookingSearchScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: HotelBookingSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    HotelBookingSearchContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

/**
 * Internal composable that represents the content of the hotel booking search screen.
 *
 * @param uiState The state of the hotel booking search screen.
 * @param onEvent The callback to be invoked when a UI event occurs.
 * @param onBackClick The callback to be invoked when the back button is clicked.
 * @param modifier The modifier to be applied to the content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HotelBookingSearchContent(
    uiState: HotelBookingSearchState,
    onEvent: (HotelBookingSearchEvent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = uiState.draft.checkInDate,
        initialSelectedEndDateMillis = uiState.draft.checkOutDate,
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

    LaunchedEffect(dateRangePickerState.selectedStartDateMillis, dateRangePickerState.selectedEndDateMillis) {
        onEvent(
            HotelBookingSearchEvent.OnDateRangeSelected(
                dateRangePickerState.selectedStartDateMillis,
                dateRangePickerState.selectedEndDateMillis
            )
        )
    }

    Scaffold(
        bottomBar = {
            TravelHotelBookingBottomBar(
                onBackClick = onBackClick,
                onNextClick = { onEvent(HotelBookingSearchEvent.OnNextClick) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            InlineErrorBanner(
                message = uiState.dateErrorMessage?.let { stringResource(it) } ?: "",
                isVisible = uiState.isDateErrorVisible,
                modifier = Modifier.padding(TravelinDimens.PaddingMedium)
            )
            TravelDateRangePicker(
                title = stringResource(R.string.booking_date_picker_title),
                state = dateRangePickerState
            )
        }
    }

    if (uiState.showGuestBottomSheet) {
        TravelGuestBottomSheet(
            adults = uiState.draft.amountOfAdults,
            kids = uiState.draft.amountOfChildren,
            hasPets = uiState.draft.amountOfPets > 0,
            onAdultsChange = { onEvent(HotelBookingSearchEvent.OnAdultsCountChange(it)) },
            onKidsChange = { onEvent(HotelBookingSearchEvent.OnChildrenCountChange(it)) },
            onHasPetsChange = { onEvent(HotelBookingSearchEvent.OnHasPetsChange(it)) },
            onAccept = { onEvent(HotelBookingSearchEvent.OnAcceptGuests) },
            onDismissRequest = { onEvent(HotelBookingSearchEvent.OnDismissGuestBottomSheet) },
            isErrorVisible = uiState.isGuestErrorVisible,
            errorMessage = uiState.guestErrorMessage?.let { stringResource(it) }
        )
    }
}

@Preview(name = "Select Dates")
@Composable
private fun HotelBookingSearchScreenPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        HotelBookingSearchContent(
            uiState = HotelBookingSearchState(),
            onEvent = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Guest Bottom Sheet")
@Composable
private fun HotelBookingGuestBottomSheetPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        HotelBookingSearchContent(
            uiState = HotelBookingSearchState(showGuestBottomSheet = true),
            onEvent = {},
            onBackClick = {}
        )
    }
}
