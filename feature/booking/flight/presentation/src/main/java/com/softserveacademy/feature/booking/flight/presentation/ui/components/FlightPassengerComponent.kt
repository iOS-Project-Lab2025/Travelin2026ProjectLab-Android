package com.softserveacademy.feature.booking.flight.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.model.BookingContactInfo
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.core.presentation.design_system.components.AppTextInput
import com.softserveacademy.core.presentation.design_system.components.TravelPhoneNumberInput
import com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.flight.domain.model.ContactError
import com.softserveacademy.feature.booking.flight.domain.model.PassengerError
import com.softserveacademy.feature.booking.flight.domain.model.PassengerFieldError
import com.softserveacademy.feature.booking.flight.presentation.ui.mappers.toDisplayName
import com.softserveacademy.feature.booking.flight.presentation.R

/**
 * Main form for an individual passenger.
 */
@Composable
fun PassengerFormItem(
    index: Int,
    total: Int,
    passenger: FlightPassenger,
    error: PassengerError?,
    onChanged: (FlightPassenger) -> Unit,
    onShowGender: () -> Unit,
    onShowDocType: () -> Unit,
    onShowNationality: () -> Unit,
    onShowDatePicker: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)) {
        // Progress Title: "Passenger 1 of 2 (Adult)"
        Text(
            text = stringResource(R.string.flight_passenger_progress_format, index + 1, total, passenger.passengerType.toDisplayName()),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = TravelinDimens.PaddingSmall)
        )

        // First Name
        PassengerField(label = stringResource(R.string.flight_first_name), isError = error?.firstNameError != null) {
            AppTextInput(
                value = passenger.firstName,
                onValueChange = { onChanged(passenger.copy(firstName = it)) },
                placeholder = stringResource(R.string.flight_first_name_placeholder),
                modifier = Modifier.fillMaxWidth(),
                state = if (error?.firstNameError != null) AppInputState.Error else AppInputState.Normal,
                errorMessage = when (error?.firstNameError) {
                    PassengerFieldError.EMPTY -> stringResource(R.string.flight_error_field_empty)
                    PassengerFieldError.TOO_SHORT -> stringResource(R.string.flight_error_too_short)
                    else -> null
                }
            )
        }

        // Last Name
        PassengerField(label = stringResource(R.string.flight_last_name), isError = error?.lastNameError != null) {
            AppTextInput(
                value = passenger.lastName,
                onValueChange = { onChanged(passenger.copy(lastName = it)) },
                placeholder = stringResource(R.string.flight_last_name_placeholder),
                modifier = Modifier.fillMaxWidth(),
                state = if (error?.lastNameError != null) AppInputState.Error else AppInputState.Normal,
                errorMessage = when (error?.lastNameError) {
                    PassengerFieldError.EMPTY -> stringResource(R.string.flight_error_field_empty)
                    PassengerFieldError.TOO_SHORT -> stringResource(R.string.flight_error_too_short)
                    else -> null
                }
            )
        }

        // Document Row
        Row(horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)) {
            PassengerSelectorField(
                label = stringResource(R.string.flight_document_type),
                value = passenger.documentType.toDisplayName(),
                placeholder = stringResource(R.string.flight_doc_placeholder),
                onClick = onShowDocType,
                isError = error?.documentError != null,
                modifier = Modifier.weight(0.4f)
            )
            PassengerField(label = stringResource(R.string.flight_document_number), isError = error?.documentError != null, modifier = Modifier.weight(0.6f)) {
                AppTextInput(
                    value = passenger.documentNumber,
                    onValueChange = { onChanged(passenger.copy(documentNumber = it)) },
                    placeholder = stringResource(R.string.flight_doc_number_placeholder),
                    state = if (error?.documentError != null) AppInputState.Error else AppInputState.Normal,
                    errorMessage = if (error?.documentError == PassengerFieldError.INVALID_FORMAT)
                        "Invalid format (No special characters)" else if (error?.documentError != null)
                        stringResource(R.string.flight_error_field_empty) else null
                )
            }
        }

        // Gender & Nationality Row
        Row(horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)) {
            PassengerSelectorField(
                label = stringResource(R.string.flight_gender),
                value = passenger.gender.toDisplayName(),
                placeholder = stringResource(R.string.flight_gender_placeholder),
                onClick = onShowGender,
                modifier = Modifier.weight(0.4f)
            )
            PassengerSelectorField(
                label = stringResource(R.string.flight_nationality),
                value = passenger.nationality,
                placeholder = stringResource(R.string.flight_country_code),
                onClick = onShowNationality,
                modifier = Modifier.weight(0.6f)
            )
        }

        // Birth Date with Dynamic Error Message
        PassengerField(label = stringResource(R.string.flight_birth_date), isError = error?.birthDateError != null) {
            DatePickerField(
                label = stringResource(R.string.flight_birth_date_placeholder),
                dateMillis = passenger.birthDateMillis,
                isError = error?.birthDateError != null,
                onClick = onShowDatePicker
            )
            if (error?.birthDateError != null) {
                Text(
                    text = if (error.birthDateError == PassengerFieldError.INVALID_AGE)
                        stringResource(R.string.flight_error_invalid_age)
                    else stringResource(R.string.flight_error_field_empty),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = TravelinDimens.PaddingMedium, top = 2.dp)
                )
            }
        }
    }
}

/**
 * Contact section. Does not include a title so the Screen can place it with the Checkbox.
 */
@Composable
fun ContactInfoSection(
    contactInfo: BookingContactInfo,
    error: ContactError?,
    enabled: Boolean = true,
    onChanged: (BookingContactInfo) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().alpha(if (enabled) 1f else 0.5f), // Gray out if disabled
        verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
    ) {
        // Email
        PassengerField(label = stringResource(R.string.flight_email), isError = error?.emailError != null) {
            AppTextInput(
                value = contactInfo.email,
                onValueChange = { if (enabled) onChanged(contactInfo.copy(email = it)) },
                placeholder = stringResource(R.string.flight_email_placeholder),
                enabled = enabled,
                modifier = Modifier.fillMaxWidth(),
                state = if (error?.emailError != null) AppInputState.Error else AppInputState.Normal,
                errorMessage = if (error?.emailError != null) stringResource(R.string.flight_error_email_invalid) else null
            )
        }

        // Phone
        PassengerField(label = "Phone Number", isError = error?.phoneError != null) {
            TravelPhoneNumberInput(
                countryCode = contactInfo.countryCode.ifBlank { "+56" },
                onCountryCodeChange = { if (enabled) onChanged(contactInfo.copy(countryCode = it)) },
                phoneNumber = contactInfo.phoneNumber,
                onPhoneNumberChange = { newValue ->
                    if (enabled) {
                        val digitsOnly = newValue.filter { it.isDigit() }
                        onChanged(contactInfo.copy(phoneNumber = digitsOnly))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                state = if (error?.phoneError != null) AppInputState.Error else AppInputState.Normal,
                //errorMessage = if (error?.phoneError != null) stringResource(R.string.flight_phone_required) else null
            )
            // Manual error message for phone as the component might not support it well when disabled
            if (error?.phoneError != null) {
                Text(
                    text = stringResource(R.string.flight_phone_required),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
            }
        }
    }
}

/**
 * Reusable wrapper for a field and its label.
 */
@Composable
fun PassengerField(
    label: String,
    isError: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        content()
    }
}

/**
 * Custom selector for non-text inputs (Gender, Doc Type, etc).
 */
@Composable
fun PassengerSelectorField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    placeholder: String,
    onClick: () -> Unit,
    isError: Boolean = false
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        Surface(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(
                width = if (isError) 2.dp else 1.dp,
                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant
            ),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = value.ifBlank { placeholder },
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (value.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}