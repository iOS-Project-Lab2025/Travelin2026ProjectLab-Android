package com.softserveacademy.feature.booking.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.presentation.components.TravelDatePickerHeadline
import com.softserveacademy.feature.booking.presentation.components.TravelHotelBookingBottomBar
import com.softserveacademy.feature.booking.presentation.components.TravelHotelBookingTopBar
import com.softserveacademy.feature.booking.presentation.components.TravelPassengerBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelHotelBookingScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
){
    val dateRangePickerState = rememberDateRangePickerState()
    var showPassengerBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TravelHotelBookingTopBar(
                onBackClick = onBackClick,
                title = stringResource(R.string.booking_topbar_title)
            )
        },
        bottomBar = {
            TravelHotelBookingBottomBar(
                onBackClick = onBackClick,
                onNextClick = { showPassengerBottomSheet = true }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { paddingValues ->
        DateRangePicker(
            state = dateRangePickerState,
            title = { Text(
                text = stringResource(R.string.booking_date_picker_title),
                modifier = Modifier
                    .padding(
                        TravelinDimens.PaddingMedium,
                        TravelinDimens.PaddingLarge,
                        TravelinDimens.PaddingMedium,
                        TravelinDimens.PaddingSmall
                    ),
                style = MaterialTheme.typography.headlineSmall
            )},
            headline = {TravelDatePickerHeadline(state = dateRangePickerState)},
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                containerColor = Color.Transparent,
                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                dayContentColor = MaterialTheme.colorScheme.onSurface,
                selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                todayContentColor = MaterialTheme.colorScheme.primary,
                todayDateBorderColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = TravelinDimens.PaddingMedium)
        )
    }

    if (showPassengerBottomSheet) {
        TravelPassengerBottomSheet(
            onDismissRequest = { showPassengerBottomSheet = false },
            onAccept = { adults, kids, pets ->
                // Handle passenger selection
                onNextClick()
            }
        )
    }
}

@Preview
@Composable
private fun TravelHotelBookingScreenPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        TravelHotelBookingScreen()
    }
}

@Preview
@Composable
private fun TravelHotelBookingScreenDarkPreview() {
    Travelin2026ProjectLabTheme(darkTheme = true) {
        TravelHotelBookingScreen()
    }
}
