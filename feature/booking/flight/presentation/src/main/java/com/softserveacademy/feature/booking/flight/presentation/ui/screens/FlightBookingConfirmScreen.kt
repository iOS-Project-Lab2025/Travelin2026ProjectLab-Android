package com.softserveacademy.feature.booking.flight.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.model.*
import com.softserveacademy.core.presentation.design_system.components.TravelLoadingScreen
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.theme.ArrowLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.CalendarIcon
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.flight.presentation.R
import com.softserveacademy.feature.booking.flight.presentation.events.FlightBookingConfirmEvent
import com.softserveacademy.feature.booking.flight.presentation.states.FlightBookingConfirmState
import com.softserveacademy.feature.booking.flight.presentation.ui.components.*
import com.softserveacademy.feature.booking.flight.presentation.util.rememberFlightDateFormatter
import com.softserveacademy.feature.booking.flight.presentation.viewmodel.FlightBookingConfirmViewModel
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import java.util.Date


/**
 * Stateful entry point for the Flight Booking Confirmation.
 * Orchestrates the review of selected flights, passenger summary, and Stripe payment processing.
 */
@Composable
fun FlightBookingConfirmScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: FlightBookingConfirmViewModel
) {
    val state by viewModel.uiState.collectAsState()

    // 1. Stripe Integration setup
    val paymentSheet = rememberPaymentSheet { result ->
        when (result) {
            is PaymentSheetResult.Completed -> viewModel.onEvent(FlightBookingConfirmEvent.OnPaymentSuccess)
            else -> viewModel.onEvent(FlightBookingConfirmEvent.OnPaymentReset)
        }
    }

    // 2. Trigger Stripe UI when clientSecret is ready
    LaunchedEffect(state.clientSecret) {
        state.clientSecret?.let { secret ->
            paymentSheet.presentWithPaymentIntent(
                secret,
                PaymentSheet.Configuration("Travelin 2026 Flights")
            )
            viewModel.onEvent(FlightBookingConfirmEvent.OnPaymentReset)
        }
    }

    // 3. Navigate forward on payment success
    LaunchedEffect(state.isPaymentSuccessful) {
        if (state.isPaymentSuccessful) onSuccess()
    }

    FlightBookingConfirmContent(
        state = state,
        onBack = onBack,
        onConfirm = { viewModel.onEvent(FlightBookingConfirmEvent.OnConfirmClick) },
        onSimulateSuccess = { viewModel.onEvent(FlightBookingConfirmEvent.OnSimulateSuccessClick) },
        onSimulateFailure = { viewModel.onEvent(FlightBookingConfirmEvent.OnSimulateFailureClick) },
        onDismissSimulation = { viewModel.onEvent(FlightBookingConfirmEvent.OnDismissPaymentSimulationSheet) }
    )
}

/**
 * Stateless UI Content for the confirmation screen.
 * Implements manual padding on specific sections to align with the Flight Cards.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FlightBookingConfirmContent(
    state: FlightBookingConfirmState,
    onBack: () -> Unit,
    onConfirm: () -> Unit,
    onSimulateSuccess: () -> Unit,
    onSimulateFailure: () -> Unit,
    onDismissSimulation: () -> Unit
) {
    val dateFormatter = rememberFlightDateFormatter()
    if (state.showPaymentSimulationSheet) {
        com.softserveacademy.feature.booking.common.presentation.ui.components.TravelPaymentSimulationSheet(
            onDismissRequest = onDismissSimulation,
            onSimulateSuccess = onSimulateSuccess,
            onSimulateFailure = onSimulateFailure,
            simulationError = state.paymentSimulationError
        )
    }
    if (state.isLoading) {
        TravelLoadingScreen()
    } else {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                ArrowLeftIcon,
                                contentDescription = stringResource(R.string.flight_back_content_description)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            },
            bottomBar = {
                FlightBookingConfirmBottomBar(
                    totalPrice = state.totalPrice,
                    currency = state.currency,
                    isLoading = state.isPaymentSheetLoading,
                    onConfirm = onConfirm
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->


            // ROOT COLUMN WITHOUT HORIZONTAL PADDING to allow Flight Cards to span correctly
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceLarge)
            ) {
                // 1. HEADER (Manual Padding)
                Column(modifier = Modifier.padding(horizontal = TravelinDimens.PaddingMedium)) {
                    Text(
                        text = stringResource(R.string.flight_confirm_booking_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.flight_confirm_booking_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 2. CRITERIA SUMMARY (Manual Padding)
                state.draft?.let { draft ->
                    Column(
                        modifier = Modifier.padding(horizontal = TravelinDimens.PaddingMedium),
                        verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
                    ) {
                        val dateText =
                            if (draft.flightType == FlightType.ROUND_TRIP && draft.startDateMillis != null && draft.endDateMillis != null) {
                                "${dateFormatter.format(Date(draft.startDateMillis!!))} - ${
                                    dateFormatter.format(
                                        Date(
                                            draft.endDateMillis!!
                                        )
                                    )
                                }"
                            } else {
                                draft.segments.firstOrNull()?.dateMillis?.let {
                                    dateFormatter.format(
                                        Date(it)
                                    )
                                } ?: ""
                            }

                        SummaryInfoCard(
                            label = stringResource(R.string.flight_confirm_dates_label),
                            value = dateText,
                            icon = { Icon(CalendarIcon, null) }
                        )
                        SummaryInfoCard(
                            label = stringResource(R.string.flight_confirm_guests_label),
                            value = stringResource(
                                R.string.flight_confirm_guests_format,
                                draft.adults,
                                draft.children,
                                draft.infants
                            )
                        )
                    }

                    // 3. FLIGHT TICKETS (NO EXTRA PADDING: FlightResultItem already has internal 16dp)
                    Column(verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)) {
                        Text(
                            text = "Flights Selected Details",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = TravelinDimens.PaddingMedium)
                        )
                        draft.selectedOffers.values.forEach { offer ->
                            FlightTicketSummaryCard(offer = offer, currencyCode = state.currency)
                        }
                    }

                    // 4. PRICE, PASSENGERS & CONTACT (Manual Padding)
                    Column(
                        modifier = Modifier.padding(horizontal = TravelinDimens.PaddingMedium),
                        verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceLarge)
                    ) {
                        // Price Breakdown
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            val totalPax = draft.adults + draft.children + draft.infants
                            val pricePerTicket =
                                if (totalPax > 0) state.totalPrice / totalPax else 0
                            Text(
                                text = stringResource(R.string.flight_confirm_price_summary_header),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(
                                    R.string.flight_confirm_price_breakdown_format,
                                    state.currency,
                                    pricePerTicket,
                                    totalPax
                                ),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Summary Cards
                        PassengerSummaryCard(passengers = draft.passengers)
                        ContactSummaryCard(contact = draft.contactInfo ?: FlightContactInfo())

                        Spacer(Modifier.height(TravelinDimens.SpaceMedium))
                    }
                }
            }
        }
    }
}


/**
 * Clean Bottom Bar showing only the Currency and Total Price.
 */
@Composable
private fun FlightBookingConfirmBottomBar(totalPrice: Int, currency: String, isLoading: Boolean, onConfirm: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(TravelinDimens.PaddingMedium).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(R.string.flight_confirm_total_price_label), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = "$currency $totalPrice",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            TravelPrimaryButton(
                text = stringResource(R.string.flight_confirm_pay_now),
                onClick = onConfirm,
                isLoading = isLoading,
                modifier = Modifier.width(160.dp)
            )
        }
    }
}

// --- PREVIEW ---

@Preview(showBackground = true, name = "Checkout Full - Professional View")
@Composable
fun FlightBookingConfirmPreview() {
    val mockAirline = Airline("LA", "LATAM Airlines", "https://picsum.photos/200")
    val mockFlight = Flight("fl1", mockAirline, "LA500", Airport("SCL", "Santiago", "SCL", "Chile"), Airport("LIM", "Lima", "LIM", "Peru"), System.currentTimeMillis(), System.currentTimeMillis() + 14400000, kotlin.time.Duration.ZERO, CabinClass.BUSINESS)
    val mockOffer = FlightOffer("off1", mockFlight, 450.0)

    val mockDraft = com.softserveacademy.feature.booking.flight.domain.model.FlightBookingDraft(
        adults = 1,
        children = 1,
        selectedOffers = mapOf(0 to mockOffer),
        passengers = listOf(
            FlightPassenger(firstName = "john", lastName = "doe", passengerType = PassengerType.ADU),
            FlightPassenger(firstName = "jane", lastName = "doe", passengerType = PassengerType.CHD)
        ),
        contactInfo = FlightContactInfo(email = "john.doe@travelin.com", phone = "987654321", countryCode = "+56")
    )

    com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            FlightBookingConfirmContent(
                state = FlightBookingConfirmState(isLoading = false, draft = mockDraft, totalPrice = 900, currency = "USD"),
                onBack = {},
                onConfirm = {},
                onSimulateSuccess = {},
                onSimulateFailure = {},
                onDismissSimulation = {}
            )
        }
    }
}