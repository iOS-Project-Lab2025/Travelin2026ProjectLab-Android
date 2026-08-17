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
import com.softserveacademy.feature.booking.tour.presentation.ui.components.TourSummaryCard
import com.softserveacademy.feature.booking.tour.presentation.viewmodel.TourBookingConfirmViewModel
import androidx.compose.runtime.LaunchedEffect

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
                        // Actually, let's use a simpler way to show dates and participants
                        androidx.compose.material3.Text(
                            text = "Dates: ${draft.startDate} - ${draft.endDate}",
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                        )
                        androidx.compose.material3.Text(
                            text = stringResource(
                                R.string.booking_confirm_participants_format,
                                draft.participants.adults,
                                draft.participants.children,
                                draft.participants.infants
                            ),
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
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
