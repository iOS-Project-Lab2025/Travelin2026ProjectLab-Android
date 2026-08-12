package com.softserveacademy.feature.booking.flight.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.model.FlightContactInfo
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.core.presentation.design_system.components.AppTextInput
import com.softserveacademy.core.presentation.design_system.components.TravelPhoneNumberInput
import com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.flight.domain.model.ContactError
import com.softserveacademy.feature.booking.flight.domain.model.PassengerError
import com.softserveacademy.feature.booking.flight.presentation.ui.mappers.toDisplayName
import com.softserveacademy.feature.booking.flight.presentation.R

/**
 * Individual form for a single passenger.
 */
@Composable
fun PassengerFormItem(
    index: Int,
    total: Int, // totalTravelers
    passenger: FlightPassenger,
    error: PassengerError?,
    onChanged: (FlightPassenger) -> Unit,
    onShowGender: () -> Unit,
    onShowDocType: () -> Unit,
    onShowDatePicker: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)) {
        // Progress Title: "Traveler 1 of 2 (Adult)"
        Text(
            text = stringResource(R.string.flight_passenger_progress_format, index + 1, total, passenger.passengerType.toDisplayName()),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = TravelinDimens.PaddingSmall)
        )

        // First Name
        PassengerField(label = stringResource(R.string.flight_first_name), isError = error?.firstNameError != null) {
            AppTextInput(
                value = passenger.firstName,
                onValueChange = { onChanged(passenger.copy(firstName = it)) },
                placeholder = "e.g. John",
                modifier = Modifier.fillMaxWidth(),
                state = if (error?.firstNameError != null) AppInputState.Error else AppInputState.Normal,
                errorMessage = if (error?.firstNameError != null) stringResource(R.string.flight_first_name_required) else null
            )
        }

        // Last Name
        PassengerField(label = stringResource(R.string.flight_last_name), isError = error?.lastNameError != null) {
            AppTextInput(
                value = passenger.lastName,
                onValueChange = { onChanged(passenger.copy(lastName = it)) },
                placeholder = "e.g. Doe",
                modifier = Modifier.fillMaxWidth(),
                state = if (error?.lastNameError != null) AppInputState.Error else AppInputState.Normal,
                errorMessage = if (error?.lastNameError != null) stringResource(R.string.flight_last_name_required) else null
            )
        }

        // Document Type and number
        Row(horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)) {
            PassengerSelectorField(
                label = stringResource(R.string.flight_document_type),
                value = passenger.documentType.toDisplayName(),
                placeholder = stringResource(R.string.flight_doc_placeholder),
                onClick = onShowDocType,
                modifier = Modifier.weight(0.4f)
            )
            PassengerField(label = stringResource(R.string.flight_document_number), isError = error?.documentError != null, modifier = Modifier.weight(0.6f)) {
                AppTextInput(
                    value = passenger.documentNumber,
                    onValueChange = { onChanged(passenger.copy(documentNumber = it)) },
                    placeholder = stringResource(R.string.flight_doc_number_placeholder),
                    state = if (error?.documentError != null) AppInputState.Error else AppInputState.Normal,
                    errorMessage = if (error?.documentError != null) stringResource(R.string.flight_document_required) else null
                )
            }
        }

        // Row Gender and Nationality
        Row(horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)) {
            PassengerSelectorField(
                label = stringResource(R.string.flight_gender),
                value = passenger.gender.toDisplayName(),
                placeholder = stringResource(R.string.flight_gender_placeholder),
                isError = false,
                onClick = onShowGender,
                modifier = Modifier.weight(0.4f)
            )
            PassengerSelectorField(
                label = stringResource(R.string.flight_nationality),
                value = passenger.nationality,
                placeholder = stringResource(R.string.flight_country_code),
                onClick = { /* TODO: Pais selector */ },
                modifier = Modifier.weight(0.6f)
            )
        }

        // Birth Date
        PassengerField(label = stringResource(R.string.flight_birth_date), isError = error?.birthDateError != null) {
            DatePickerField(
                label = stringResource(R.string.flight_birth_date_placeholder),
                dateMillis = passenger.birthDateMillis,
                isError = error?.birthDateError != null,
                onClick = onShowDatePicker
            )
            if (error?.birthDateError != null) {
                Text(
                    text = stringResource(R.string.flight_birth_date_required),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = TravelinDimens.PaddingMedium, top = 2.dp)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = TravelinDimens.PaddingSmall),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    }
}

/**
 * Section for primary contact information.
 */
@Composable
fun ContactInfoSection(
    contactInfo: FlightContactInfo,
    error: ContactError?,
    onChanged: (FlightContactInfo) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)) {
        Text(
            text = stringResource(R.string.flight_contact_details),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = TravelinDimens.PaddingSmall)
        )

        PassengerField(label = "Email Address", isError = error?.emailError != null) {
            AppTextInput(
                value = contactInfo.email,
                onValueChange = { onChanged(contactInfo.copy(email = it)) },
                placeholder = "your@email.com",
                modifier = Modifier.fillMaxWidth(),
                state = if (error?.emailError != null) AppInputState.Error else AppInputState.Normal,
                errorMessage = if (error?.emailError != null) stringResource(R.string.flight_email_required) else null
            )

        }

        PassengerField(label = "Phone Number", isError = error?.phoneError != null) {
            TravelPhoneNumberInput(
                countryCode = contactInfo.countryCode.ifBlank { stringResource(R.string.flight_country_code) },
                onCountryCodeChange = { onChanged(contactInfo.copy(countryCode = it)) },
                phoneNumber = contactInfo.phone,
                onPhoneNumberChange = { onChanged(contactInfo.copy(phone = it)) },
                modifier = Modifier.fillMaxWidth(),
                state = if (error?.phoneError != null) AppInputState.Error else AppInputState.Normal,
                errorMessage = if (error?.phoneError != null) stringResource(R.string.flight_phone_required) else null
            )
        }
    }
}

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

@Composable
fun PassengerSelectorField(
    label: String,
    value: String,
    placeholder: String,
    onClick: () -> Unit,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)) {
        Text(text = label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        Surface(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
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