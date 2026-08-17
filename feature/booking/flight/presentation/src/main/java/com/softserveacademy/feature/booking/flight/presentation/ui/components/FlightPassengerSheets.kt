package com.softserveacademy.feature.booking.flight.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.model.DocumentType
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.core.domain.model.Gender
import com.softserveacademy.core.presentation.design_system.components.Country
import com.softserveacademy.core.presentation.design_system.components.countries
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.ui.mappers.toDisplayName

/**
 * Bottom sheet to select Gender. Uses the unified card style.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelectionSheet(
    isVisible: Boolean,
    selectedGender: Gender?,
    onGenderSelected: (Gender) -> Unit,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        ModalBottomSheet(onDismissRequest = onDismiss, containerColor = MaterialTheme.colorScheme.surface) {
            Column(modifier = Modifier.padding(TravelinDimens.PaddingMedium).fillMaxWidth().padding(bottom = TravelinDimens.PaddingLarge)) {
                Text(text = stringResource(R.string.flight_gender_placeholder), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(TravelinDimens.SpaceMedium))
                Gender.entries.forEach { gender ->
                    OptionCard(
                        text = gender.toDisplayName(),
                        isSelected = selectedGender == gender,
                        onClick = { onGenderSelected(gender); onDismiss() }
                    )
                }
            }
        }
    }
}

/**
 * Bottom sheet to select Document Type. Uses the unified card style.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentTypeSelectionSheet(
    isVisible: Boolean,
    selectedType: DocumentType,
    onTypeSelected: (DocumentType) -> Unit,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        ModalBottomSheet(onDismissRequest = onDismiss, containerColor = MaterialTheme.colorScheme.surface) {
            Column(modifier = Modifier.padding(TravelinDimens.PaddingMedium).fillMaxWidth().padding(bottom = TravelinDimens.PaddingLarge)) {
                Text(text = stringResource(R.string.flight_document_type), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(TravelinDimens.SpaceMedium))
                DocumentType.entries.forEach { type ->
                    OptionCard(
                        text = type.toDisplayName(),
                        isSelected = selectedType == type,
                        onClick = { onTypeSelected(type); onDismiss() }
                    )
                }
            }
        }
    }
}

/**
 * Bottom sheet to select Nationality. Uses the unified card style.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NationalitySelectionSheet(
    isVisible: Boolean,
    selectedNationality: String,
    onCountrySelected: (Country) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (isVisible) {
        ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState, containerColor = MaterialTheme.colorScheme.surface, dragHandle = { BottomSheetDefaults.DragHandle() }) {
            Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f).padding(horizontal = TravelinDimens.PaddingMedium).navigationBarsPadding()) {
                Text(text = stringResource(R.string.flight_select_nationality), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(countries) { country ->
                        val countryFullName = "${country.flag} ${country.name}"
                        OptionCard(
                            text = countryFullName,
                            isSelected = selectedNationality == countryFullName, // RESALTADO
                            onClick = { onCountrySelected(country); onDismiss() }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Bottom sheet to select Birth Date. Uses the unified card style.
 */

@Composable
private fun OptionCard(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(56.dp).clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        ),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
    ) {
        Box(modifier = Modifier.padding(horizontal = 16.dp).fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Text(text = text, style = MaterialTheme.typography.bodyLarge, color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
        }
    }
}