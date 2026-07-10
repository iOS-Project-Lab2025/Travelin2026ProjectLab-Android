package com.softserveacademy.feature.booking.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.theme.CalendarIcon
import com.softserveacademy.core.presentation.design_system.theme.EditIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.presentation.R

/**
 * A custom date range picker component that uses Material 3's [DateRangePicker].
 *
 * @param title The title to be displayed above the date range picker.
 * @param state The state of the date range picker.
 * @param modifier The modifier to be applied to the date range picker.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelDateRangePicker(
    title: String,
    state: DateRangePickerState,
    modifier: Modifier = Modifier
) {
    DateRangePicker(
        state = state,
        title = {
            TravelDateRangePickerTitle(
                title = title,
                state = state,
                modifier = Modifier
            )
        },
        headline = {
            TravelDateRangePickerHeadline(
                state = state,
                modifier = Modifier
            )
        },
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
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = TravelinDimens.PaddingMedium)
    )
}

/**
 * Internal composable for the title section of the [TravelDateRangePicker].
 *
 * @param title The title text.
 * @param state The state of the date range picker.
 * @param modifier The modifier to be applied to the title section.
 */
@Composable
fun TravelDateRangePickerTitle(
    title: String,
    state: DateRangePickerState,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(TravelinDimens.PaddingSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )
        val displayIcon = if (state.displayMode == DisplayMode.Picker) EditIcon else CalendarIcon
        TravelIconButton(
            icon = displayIcon,
            onClick = {
                state.displayMode = if (state.displayMode == DisplayMode.Picker) {
                    DisplayMode.Input
                } else {
                    DisplayMode.Picker
                }
            },
            showBackground = false
        )
    }
}

/**
 * Internal composable for the headline section of the [TravelDateRangePicker].
 *
 * @param state The state of the date range picker.
 * @param modifier The modifier to be applied to the headline section.
 */
@Composable
fun TravelDateRangePickerHeadline(
    state: DateRangePickerState,
    modifier: Modifier = Modifier,
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
fun TravelDateRangePickerPreview(){
    Travelin2026ProjectLabTheme(darkTheme = false) {
        TravelDateRangePicker(
            title = stringResource(R.string.booking_date_picker_title),
            state = rememberDateRangePickerState(),
            modifier = Modifier
        )
    }
}