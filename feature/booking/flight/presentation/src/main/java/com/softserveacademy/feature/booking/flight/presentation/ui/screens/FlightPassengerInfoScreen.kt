package com.softserveacademy.feature.booking.flight.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
 * Manages the Wizard-style navigation through multiple passenger forms and
 * handles primary contact synchronization.
 */
@Composable
fun FlightPassengerInfoScreen(
    viewModel: FlightPassengerInfoViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Handle Forward Navigation (to Confirmation/Checkout)
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { onNext() }
    }

    // Handle Backward Navigation (to Flight Results or Previous Step)
    LaunchedEffect(Unit) {
        viewModel.navigationBackEvent.collect { onBack() }
    }

    FlightPassengerInfoContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

/**
 * Stateless UI for the Passenger Information form.
 * Implements a unified layout with Passenger details and Contact section below.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightPassengerInfoContent(
    state: FlightPassengerInfoState,
    onEvent: (FlightPassengerInfoEvent) -> Unit,
) {
    if (state.isLoading) {
        TravelLoadingScreen()
    } else {
        Scaffold(
            bottomBar = {
                TravelBookingBottomBar(
                    onBackClick = { onEvent(FlightPassengerInfoEvent.OnBackClick) },
                    onNextClick = { onEvent(FlightPassengerInfoEvent.OnNextClick) }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            val currentIndex = state.currentPassengerIndex
            val currentPassenger = state.passengers.getOrNull(currentIndex) ?: return@Scaffold

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = TravelinDimens.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
            ) {
                item {
                    Spacer(Modifier.height(TravelinDimens.SpaceSmall))

                    // 1. INDIVIDUAL PASSENGER FORM
                    PassengerFormItem(
                        index = currentIndex,
                        total = state.passengers.size,
                        passenger = currentPassenger,
                        error = state.passengerErrors[currentIndex],
                        onChanged = { updated ->
                            onEvent(FlightPassengerInfoEvent.OnPassengerDataChanged(currentIndex, updated))
                        },
                        onShowGender = { onEvent(FlightPassengerInfoEvent.OnShowGenderSheet(currentIndex)) },
                        onShowDocType = { onEvent(FlightPassengerInfoEvent.OnShowDocTypeSheet(currentIndex)) },
                        onShowNationality = { onEvent(FlightPassengerInfoEvent.OnShowNationalitySheet(currentIndex)) },
                        onShowDatePicker = { onEvent(FlightPassengerInfoEvent.OnShowDatePicker(currentIndex)) }
                    )

                    Spacer(Modifier.height(TravelinDimens.SpaceLarge))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(Modifier.height(TravelinDimens.SpaceLarge))

                    // 2. CONTACT DETAILS HEADER & CHECKBOX
                    Text(
                        text = stringResource(R.string.flight_contact_details),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = TravelinDimens.PaddingSmall)
                    )

                    // Show "Use Primary Contact" starting from the second passenger
                    if (currentIndex > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(bottom = TravelinDimens.PaddingMedium)
                        ) {
                            Checkbox(
                                checked = state.usePrimaryContact,
                                onCheckedChange = { onEvent(FlightPassengerInfoEvent.OnToggleSameContact(it)) }
                            )
                            Text(
                                text = stringResource(R.string.flight_use_primary_contact),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // 3. CONTACT INFO SECTION (Grayed out if checkbox is enabled)
                    ContactInfoSection(
                        contactInfo = state.contactInfo,
                        error = state.contactError,
                        enabled = !state.usePrimaryContact, // Controlled by ViewModel state
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
            selectedGender = passenger.gender,
            onGenderSelected = { gender ->
                val updated = passenger.copy(gender = gender)
                onEvent(FlightPassengerInfoEvent.OnPassengerDetailSelected(state.activePassengerIndex, updated))
            },
            onDismiss = { onEvent(FlightPassengerInfoEvent.OnDismissSheet) }
        )

        DocumentTypeSelectionSheet(
            isVisible = state.showDocTypeSheet,
            selectedType = passenger.documentType,
            onTypeSelected = { type ->
                val updated = passenger.copy(documentType = type)
                onEvent(FlightPassengerInfoEvent.OnPassengerDetailSelected(state.activePassengerIndex, updated))
            },
            onDismiss = { onEvent(FlightPassengerInfoEvent.OnDismissSheet) }
        )

        NationalitySelectionSheet(
            isVisible = state.showNationalitySheet,
            selectedNationality = passenger.nationality,
            onCountrySelected = { country ->
                val updated = passenger.copy(nationality = "${country.flag} ${country.name}")
                onEvent(FlightPassengerInfoEvent.OnPassengerDetailSelected(state.activePassengerIndex, updated))
            },
            onDismiss = { onEvent(FlightPassengerInfoEvent.OnDismissSheet) }
        )

        // Birth Date Picker Dialog
        if (state.showDatePicker) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = passenger.birthDateMillis)

            ModalBottomSheet(
                onDismissRequest = { onEvent(FlightPassengerInfoEvent.OnDismissSheet) },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.background,
                dragHandle = { BottomSheetDefaults.DragHandle() },
                modifier = Modifier.fillMaxSize()
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
                            colors = DatePickerDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                selectedYearContainerColor = MaterialTheme.colorScheme.primaryContainer
                            )
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
                                        passenger.copy(birthDateMillis = datePickerState.selectedDateMillis)
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
            birthDateMillis = System.currentTimeMillis() - 946080000000L
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
            FlightPassengerInfoContent(state = previewState, onEvent = {})
        }
    }
}

@Preview(showBackground = true, name = "Passenger Info - Dark Mode")
@Composable
fun FlightPassengerInfoDarkPreview() {
    com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            FlightPassengerInfoContent(state = previewState, onEvent = {})
        }
    }
}