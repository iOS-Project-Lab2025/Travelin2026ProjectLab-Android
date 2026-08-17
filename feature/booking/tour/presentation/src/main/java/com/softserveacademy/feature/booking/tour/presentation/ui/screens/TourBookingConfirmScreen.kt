package com.softserveacademy.feature.booking.tour.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.presentation.design_system.components.TravelLoadingScreen
import com.softserveacademy.core.presentation.design_system.components.countries
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.common.presentation.ui.components.TravelBookingContactInfoCard
import com.softserveacademy.feature.booking.common.presentation.ui.components.TravelPaymentSimulationSheet
import com.softserveacademy.feature.booking.common.presentation.ui.screens.TravelBookingConfirmScreen
import com.softserveacademy.feature.booking.tour.presentation.R
import com.softserveacademy.feature.booking.tour.presentation.events.TourBookingConfirmEvent
import com.softserveacademy.feature.booking.tour.presentation.states.TourBookingConfirmState
import com.softserveacademy.feature.booking.tour.presentation.ui.components.TourBookingSummaryCard
import com.softserveacademy.feature.booking.tour.presentation.ui.components.TourSummaryCard
import com.softserveacademy.feature.booking.tour.presentation.viewmodel.TourBookingConfirmViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.domain.model.BookingContactInfo
import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.feature.booking.tour.domain.model.Participants
import com.softserveacademy.feature.booking.tour.domain.model.TourBookingDraft

@Composable
fun TourBookingConfirmScreen(
    onBackClick: () -> Unit,
    onPaymentSuccess: () -> Unit,
    viewModel: TourBookingConfirmViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isPaymentSuccessful) {
        if (uiState.isPaymentSuccessful) {
            onPaymentSuccess()
        }
    }

    TourBookingConfirmContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onConfirmClick = { viewModel.onEvent(TourBookingConfirmEvent.OnConfirmClick) },
        onRetryClick = { viewModel.onEvent(TourBookingConfirmEvent.OnRetryClick) },
        onSimulateSuccess = { viewModel.onEvent(TourBookingConfirmEvent.OnSimulateSuccessClick) },
        onSimulateFailure = { viewModel.onEvent(TourBookingConfirmEvent.OnSimulateFailureClick) },
        onDismissBottomSheet = { viewModel.onEvent(TourBookingConfirmEvent.OnDismissPaymentSimulationSheet) }
    )
}

@Composable
fun TourBookingConfirmContent(
    uiState: TourBookingConfirmState,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onRetryClick: () -> Unit,
    onSimulateSuccess: () -> Unit = {},
    onSimulateFailure: () -> Unit = {},
    onDismissBottomSheet: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    if (uiState.showPaymentSimulationSheet) {
        TravelPaymentSimulationSheet(
            onDismissRequest = onDismissBottomSheet,
            onSimulateSuccess = onSimulateSuccess,
            onSimulateFailure = onSimulateFailure,
            simulationError = uiState.paymentSimulationError
        )
    }

    if (uiState.isLoading) {
        TravelLoadingScreen()
    } else {
        TravelBookingConfirmScreen(
            totalPrice = uiState.totalPrice.toInt(),
            onBackClick = onBackClick,
            onConfirmClick = onConfirmClick,
            isConfirmLoading = uiState.isPaymentSheetLoading,
            error = uiState.error,
            onRetryClick = onRetryClick
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(TravelinDimens.PaddingMedium)
            ) {
                uiState.tour?.let { tour ->
                    TourSummaryCard(tour = tour)

                    Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

                    // Booking Details Section
                    uiState.bookingDraft?.let { draft ->
                        val participantsString = stringResource(
                            R.string.booking_confirm_participants_format,
                            draft.participants.adults,
                            draft.participants.children,
                            draft.participants.infants
                        )
                        TourBookingSummaryCard(
                            startDate = draft.startDate ?: 0L,
                            endDate = draft.endDate,
                            participants = participantsString
                        )
                    }

                    Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

                    // Contact Information
                    val contactInfo = uiState.bookingDraft?.contactInfo
                    val countryCode = contactInfo?.countryCode ?: ""
                    TravelBookingContactInfoCard(
                        firstName = contactInfo?.firstName ?: "",
                        lastName = contactInfo?.lastName ?: "",
                        email = contactInfo?.email ?: "",
                        countryCode = countryCode,
                        countryFlag = countries.find { it.code == countryCode }?.flag ?: "",
                        phoneNumber = contactInfo?.phoneNumber ?: "",
                        subtitle = stringResource(R.string.contact_info_subtitle)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TourBookingConfirmPreview() {
    val sampleTour = Tour(
        id = "1",
        title = "Bali Adventure Tour",
        location = "Bali, Indonesia",
        rating = 4.8,
        numberOfReviews = 150,
        imageList = listOf("https://picsum.photos/800/600"),
        includedServices = listOf("TOUR_TRANSPORT", "TOUR_GUIDE", "TOUR_BREAKFAST")
    )

    val sampleDraft = TourBookingDraft(
        tourId = "1",
        startDate = 1782115200000L, // Thursday, July 23, 2026
        endDate = 1782374400000L, // Sunday, July 26, 2026
        participants = Participants(adults = 2, children = 1),
        contactInfo = BookingContactInfo(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@gmail.com",
            countryCode = "+1",
            phoneNumber = "123 456 789"
        )
    )

    Travelin2026ProjectLabTheme {
        TourBookingConfirmContent(
            uiState = TourBookingConfirmState(
                tour = sampleTour,
                bookingDraft = sampleDraft,
                totalPrice = 450.0
            ),
            onBackClick = {},
            onConfirmClick = {},
            onRetryClick = {}
        )
    }
}
