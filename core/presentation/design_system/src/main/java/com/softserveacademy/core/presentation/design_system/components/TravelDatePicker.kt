package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelDatePicker(
    selectedDate: Long?,
    onDateSelected: (Long) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    state: AppInputState = AppInputState.Normal,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    var showDialog by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed && enabled) {
            showDialog = true
        }
    }

    val formattedDate = remember(selectedDate) {
        if (selectedDate != null) {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.format(Date(selectedDate))
        } else {
            ""
        }
    }

    Box(modifier = modifier) {
        AppTextInput(
            value = formattedDate,
            onValueChange = { },
            placeholder = placeholder,
            modifier = Modifier.fillMaxWidth(),
            state = state,
            errorMessage = errorMessage,
            enabled = enabled,
            interactionSource = interactionSource,
            readOnly = true
        )
    }

    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelDatePickerPreview() {
    Travelin2026ProjectLabTheme {
        TravelDatePicker(
            selectedDate = 839827200000L, // Aug 12, 1996
            onDateSelected = {},
            placeholder = "Select Date"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TravelDatePickerDialogPreview() {
    Travelin2026ProjectLabTheme {
        DatePickerDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = { }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { }) {
                    Text("Cancel")
                }
            }
        ) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = 839827200000L // Aug 12, 1996
            )
            DatePicker(state = datePickerState)
        }
    }
}
