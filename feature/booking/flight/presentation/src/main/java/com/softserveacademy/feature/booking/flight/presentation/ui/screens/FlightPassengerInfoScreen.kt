package com.softserveacademy.feature.booking.flight.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.model.FlightContactInfo
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.core.presentation.design_system.components.TravelLoadingScreen
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.common.presentation.ui.components.TravelBookingBottomBar
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.events.FlightPassengerInfoEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightPassengerInfoState
import com.softserveacademy.feature.booking.flight.presentation.ui.components.*
import com.softserveacademy.feature.booking.flight.presentation.viewmodel.FlightPassengerInfoViewModel

/**
 * Stateful entry point for the Traveler Details collection (US3).
 * Orchestrates the passenger data forms and contact information validation.
 *
 * @param viewModel State holder for the passenger forms.
 * @param onNext Navigation callback to progress to check out/payment.
 * @param onBack Navigation callback to return to flight selection.
 */
@Composable
fun FlightPassengerInfoScreen(
    viewModel: FlightPassengerInfoViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Observe navigation events from ViewModel
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { onNext() }
    }

    FlightPassengerInfoContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack
    )
}

/**
 * Stateless UI for the Passenger Information form.
 * Displays a list of forms based on the number of travelers and a single contact section.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightPassengerInfoContent(
    state: FlightPassengerInfoState,
    onEvent: (FlightPassengerInfoEvent) -> Unit,
    onBack: () -> Unit
) {
    if (state.isLoading) {
        TravelLoadingScreen()
    } else {
        Scaffold(
            bottomBar = {
                TravelBookingBottomBar(
                    onBackClick = onBack,
                    onNextClick = { onEvent(FlightPassengerInfoEvent.OnNextClick) }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = TravelinDimens.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceLarge)
            ) {
                // Header Section
                item {
                    Spacer(Modifier.height(TravelinDimens.SpaceMedium))
                    Text(
                        text = stringResource(R.string.flight_passengers_title),
                        style = MaterialTheme.typography.headlineSmall, // Professional compact title
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.flight_passengers_subtitle),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Dynamic forms for each traveler (Adults, Children, Infants)
                itemsIndexed(state.passengers) { index, passenger ->
                    PassengerFormItem(
                        index = index,
                        total = state.passengers.size,
                        passenger = passenger,
                        error = state.passengerErrors[index],
                        onChanged = { onEvent(FlightPassengerInfoEvent.OnPassengerDataChanged(index, it)) },
                        onShowGender = { onEvent(FlightPassengerInfoEvent.OnShowGenderSheet(index)) },
                        onShowDocType = { onEvent(FlightPassengerInfoEvent.OnShowDocTypeSheet(index)) },
                        onShowDatePicker = { onEvent(FlightPassengerInfoEvent.OnShowDatePicker(index)) }
                    )
                }

                // Global contact information section
                item {
                    ContactInfoSection(
                        contactInfo = state.contactInfo,
                        error = state.contactError,
                        onChanged = { onEvent(FlightPassengerInfoEvent.OnContactInfoChanged(it)) }
                    )
                    Spacer(Modifier.height(TravelinDimens.SpaceExtraLarge))
                }
            }
        }
    }

    // Modal Interaction Handlers (BottomSheets)
    val activePassenger = state.passengers.getOrNull(state.activePassengerIndex)
    activePassenger?.let { passenger ->
        GenderSelectionSheet(
            isVisible = state.showGenderSheet,
            passenger = passenger,
            onGenderSelected = { onEvent(FlightPassengerInfoEvent.OnPassengerDetailSelected(state.activePassengerIndex, passenger.copy(gender = it))) },
            onDismiss = { onEvent(FlightPassengerInfoEvent.OnDismissSheet) }
        )

        DocumentTypeSelectionSheet(
            isVisible = state.showDocTypeSheet,
            passenger = passenger,
            onTypeSelected = { onEvent(FlightPassengerInfoEvent.OnPassengerDetailSelected(state.activePassengerIndex, passenger.copy(documentType = it))) },
            onDismiss = { onEvent(FlightPassengerInfoEvent.OnDismissSheet) }
        )

        // Unified DatePicker for Passenger Birth Date
        if (state.showDatePicker) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            val datePickerState = rememberDatePickerState()

            ModalBottomSheet(
                onDismissRequest = { onEvent(FlightPassengerInfoEvent.OnDismissSheet) },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.background,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                Column(modifier = Modifier.fillMaxSize().navigationBarsPadding()) {
                    Text(
                        text = stringResource(R.string.flight_select_birth_date),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(TravelinDimens.PaddingMedium)
                    )

                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        DatePicker(
                            state = datePickerState,
                            title = null, headline = null,
                            showModeToggle = false,
                            modifier = Modifier.fillMaxWidth(),
                            colors = DatePickerDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
                        )
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        tonalElevation = TravelinDimens.ElevationSmall,
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Box(modifier = Modifier.padding(TravelinDimens.PaddingMedium)) {
                            TravelPrimaryButton(
                                text = stringResource(com.softserveacademy.feature.booking.common.presentation.R.string.accept_button_label),
                                onClick = {
                                    onEvent(FlightPassengerInfoEvent.OnPassengerDetailSelected(
                                        state.activePassengerIndex,
                                        activePassenger.copy(birthDateMillis = datePickerState.selectedDateMillis)
                                    ))
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- PREVIEWS (LIGHT & DARK MODES) ---

/**
 * Realistic mock data for the preview state.
 */
private val previewState = FlightPassengerInfoState(
    isLoading = false,
    passengers = listOf(
        FlightPassenger(
            firstName = "John",
            lastName = "Doe",
            documentNumber = "12345678-9",
            passengerType = com.softserveacademy.core.domain.model.PassengerType.ADU,
            gender = com.softserveacademy.core.domain.model.Gender.MALE,
            nationality = "Chile",
            birthDateMillis = System.currentTimeMillis() - 946080000000L // Approx 30 years old
        )
    ),
    contactInfo = FlightContactInfo(
        email = "john.doe@travelin.com",
        phone = "987654321",
        countryCode = "+56"
    )
)

@Preview(showBackground = true, name = "Passenger Info - Light Mode")
@Composable
fun FlightPassengerInfoPreview() {
    com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            FlightPassengerInfoContent(
                state = previewState,
                onEvent = {},
                onBack = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Passenger Info - Dark Mode")
@Composable
fun FlightPassengerInfoDarkPreview() {
    com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            FlightPassengerInfoContent(
                state = previewState,
                onEvent = {},
                onBack = {}
            )
        }
    }
}