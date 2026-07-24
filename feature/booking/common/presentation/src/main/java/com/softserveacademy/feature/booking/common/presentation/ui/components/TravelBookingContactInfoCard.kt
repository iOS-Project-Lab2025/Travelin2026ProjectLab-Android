package com.softserveacademy.feature.booking.common.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.common.presentation.R

/**
 * A summary card displaying contact information for a booking.
 *
 * All fields are read-only and are not editable.
 *
 * @param modifier The modifier to be applied to the card.
 * @param firstName The first name of the contact.
 * @param lastName The last name of the contact.
 * @param email The email address of the contact.
 * @param countryCode The country code for the phone number.
 * @param countryFlag The flag emoji for the country.
 * @param phoneNumber The phone number of the contact.
 * @param subtitle An optional subtitle for the card.
 */
@Composable
fun TravelBookingContactInfoCard(
    modifier: Modifier = Modifier,
    firstName: String,
    lastName: String,
    email: String,
    countryCode: String,
    countryFlag: String,
    phoneNumber: String,
    subtitle: String? = null,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = TravelinDimens.ElevationSmall
        )
    ) {
        Column(
            modifier = Modifier.padding(TravelinDimens.PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
        ) {
            // Contact Information Title
            Text(
                text = stringResource(R.string.contact_info_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle?: stringResource(id = R.string.contact_info_subtitle),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))

            // First Name
            ContactField(
                label = stringResource(R.string.contact_info_first_name),
                value = firstName
            )

            // Last Name
            ContactField(
                label = stringResource(R.string.contact_info_last_name),
                value = lastName
            )

            // Phone
            Text(
                text = stringResource(R.string.contact_info_phone),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = "$countryFlag $countryCode",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true
                )
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.weight(2.5f),
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true
                )
            }

            // Email
            ContactField(
                label = stringResource(R.string.contact_info_email),
                value = email
            )
        }
    }
}

/**
 * Internal composable to display a contact field with a label and value (Read-only).
 *
 * @param label The label text for the contact field.
 * @param value The value text for the contact field.
 */
@Composable
private fun ContactField(
    label: String,
    value: String
) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold
    )
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun TravelBookingContactInfoCardPreview() {
    Travelin2026ProjectLabTheme {
        TravelBookingContactInfoCard(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@gmail.com",
            countryCode = "+1",
            countryFlag = "🇺🇸",
            phoneNumber = "123 456 789",
            modifier = Modifier.padding(TravelinDimens.PaddingMedium)
        )
    }
}
