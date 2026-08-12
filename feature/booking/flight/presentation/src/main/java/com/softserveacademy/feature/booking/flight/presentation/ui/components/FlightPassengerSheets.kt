package com.softserveacademy.feature.booking.flight.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.softserveacademy.core.domain.model.DocumentType
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.core.domain.model.Gender
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.flight.presentation.ui.mappers.toDisplayName

/**
 * Bottom sheet to select Gender for a specific passenger.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelectionSheet(
    isVisible: Boolean,
    passenger: FlightPassenger,
    onGenderSelected: (Gender) -> Unit,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column(modifier = Modifier.padding(TravelinDimens.PaddingMedium).navigationBarsPadding()) {
                Text(
                    text = "Select Gender",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = TravelinDimens.PaddingMedium)
                )
                Gender.entries.forEach { gender ->
                    ListItem(
                        headlineContent = { Text(gender.toDisplayName()) },
                        modifier = Modifier.clickable { onGenderSelected(gender) }
                    )
                }
            }
        }
    }
}

/**
 * Bottom sheet to select the Document Type (Passport, etc).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentTypeSelectionSheet(
    isVisible: Boolean,
    passenger: FlightPassenger,
    onTypeSelected: (DocumentType) -> Unit,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column(modifier = Modifier.padding(TravelinDimens.PaddingMedium).navigationBarsPadding()) {
                Text(
                    text = "Document Type",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = TravelinDimens.PaddingMedium)
                )
                DocumentType.entries.forEach { type ->
                    ListItem(
                        headlineContent = { Text(type.toDisplayName()) },
                        modifier = Modifier.clickable { onTypeSelected(type) }
                    )
                }
            }
        }
    }
}