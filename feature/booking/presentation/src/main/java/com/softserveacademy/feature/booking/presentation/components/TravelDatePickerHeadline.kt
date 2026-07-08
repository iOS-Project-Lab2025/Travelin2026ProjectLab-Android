package com.softserveacademy.feature.booking.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.CalendarIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@Composable
fun TravelDatePickerHeadline(
    modifier: Modifier = Modifier,
    state: DateRangePickerState,
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(TravelinDimens.PaddingLarge),
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            CalendarIcon,
            "Calendar Icon",
            tint = MaterialTheme.colorScheme.primary
        )
        DateRangePickerDefaults.DateRangePickerHeadline(
            selectedStartDateMillis = state.selectedStartDateMillis,
            selectedEndDateMillis = state.selectedEndDateMillis,
            displayMode = state.displayMode,
            dateFormatter = DatePickerDefaults.dateFormatter(),
            modifier = Modifier.padding(TravelinDimens.PaddingSmall, 0.dp),
            contentColor = MaterialTheme.colorScheme.primary,
        )
        Icon(
            CalendarIcon,
            "Calendar Icon",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun TravelDatePickerHeadlinePreview() {
    Travelin2026ProjectLabTheme {
        val state = rememberDateRangePickerState()
        DateRangePicker(
            state = state,
            title = null,
            headline = {TravelDatePickerHeadline(modifier = Modifier, state = state)},
            showModeToggle = false,
        )
    }
}