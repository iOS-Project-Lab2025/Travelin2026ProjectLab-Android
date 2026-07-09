package com.softserveacademy.feature.booking.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.feature.booking.presentation.components.TravelDateRangePicker
import com.softserveacademy.feature.booking.presentation.components.TravelGuestBottomSheet
import com.softserveacademy.feature.booking.presentation.components.TravelHotelBookingBottomBar
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelHotelBookingScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
){
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val dateRangePickerState = rememberDateRangePickerState(
        yearRange = IntRange(currentYear, currentYear + 3),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Get the start of today in UTC
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
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            TravelHotelBookingBottomBar(
                onBackClick = onBackClick,
                onNextClick = { showBottomSheet = true }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { paddingValues ->
        TravelDateRangePicker(
            title = stringResource(R.string.booking_date_picker_title),
            state = dateRangePickerState,
            modifier = Modifier.padding(paddingValues)
        )
    }

    if (showBottomSheet) {
        TravelGuestBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            onAccept = { adults, kids, hasPets ->
                // Handle guests selection
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
