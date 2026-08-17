package com.softserveacademy.feature.booking.tour.presentation.ui.screens

import androidx.activity.compose.BackHandler
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
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import com.softserveacademy.core.presentation.design_system.theme.BlueDark80
import com.softserveacademy.core.presentation.design_system.theme.Gray40
import com.softserveacademy.core.presentation.design_system.theme.Gray80
import com.softserveacademy.core.presentation.design_system.theme.GrayLight20
import com.softserveacademy.core.presentation.design_system.theme.Red50
import com.softserveacademy.core.presentation.design_system.theme.Teal40
import com.softserveacademy.core.presentation.design_system.theme.White100
import com.softserveacademy.core.presentation.design_system.R as coreR

@Composable
fun TourBookingConfirmScreen(
    onBackClick: () -> Unit,
    onPaymentSuccess: () -> Unit,
    viewModel: TourBookingConfirmViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        viewModel.onEvent(TourBookingConfirmEvent.OnBackClick)
        onBackClick()
    }

    val paymentSheet = rememberPaymentSheet { result ->
        when (result) {
            is PaymentSheetResult.Completed -> {
                viewModel.onEvent(TourBookingConfirmEvent.OnPaymentSuccess)
            }
            is PaymentSheetResult.Canceled -> {
                viewModel.onEvent(TourBookingConfirmEvent.OnPaymentReset)
            }
            is PaymentSheetResult.Failed -> {
                viewModel.onEvent(TourBookingConfirmEvent.OnPaymentReset)
            }
        }
    }

    LaunchedEffect(uiState.clientSecret) {
        uiState.clientSecret?.let { secret ->
            paymentSheet.presentWithPaymentIntent(
                secret,
                PaymentSheet.Configuration(
                    merchantDisplayName = "Travelin 2026",
                    appearance = PaymentSheet.Appearance(
                        colorsLight = PaymentSheet.Colors(
                            primary = Teal40,
                            surface = White100,
                            component = White100,
                            componentBorder = Gray40,
                            componentDivider = Gray40,
                            onComponent = Gray80,
                            subtitle = Gray40,
                            placeholderText = Gray40,
                            onSurface = Gray80,
                            appBarIcon = Gray80,
                            error = Red50,
                        ),
                        colorsDark = PaymentSheet.Colors(
                            primary = Teal40,
                            surface = BlueDark80,
                            component = BlueDark80,
                            componentBorder = Gray80,
                            componentDivider = Gray40,
                            onComponent = GrayLight20,
                            subtitle = Gray40,
                            placeholderText = Gray40,
                            onSurface = GrayLight20,
                            appBarIcon = GrayLight20,
                            error = Red50,
                        ),
                        shapes = PaymentSheet.Shapes(
                            cornerRadiusDp = 10f,
                            borderStrokeWidthDp = 1f
                        ),
                        typography = PaymentSheet.Typography(
                            sizeScaleFactor = 1f,
                            fontResId = coreR.font.inter_medium,
                        ),
                        primaryButton = PaymentSheet.PrimaryButton(
                            shape = PaymentSheet.PrimaryButtonShape(
                                cornerRadiusDp = 15f,
                                heightDp = 56f
                            ),
                        )
                    )
                )
            )
            viewModel.onEvent(TourBookingConfirmEvent.OnPaymentReset)
        }
    }

    LaunchedEffect(uiState.isPaymentSuccessful) {
        if (uiState.isPaymentSuccessful) {
            onPaymentSuccess()
        }
    }

    TourBookingConfirmContent(
        uiState = uiState,
        onBackClick = {
            viewModel.onEvent(TourBookingConfirmEvent.OnBackClick)
            onBackClick()
        },
        onConfirmClick = { viewModel.onEvent(TourBookingConfirmEvent.OnConfirmClick) },
        onRetryClick = { viewModel.onEvent(TourBookingConfirmEvent.OnRetryClick) },
        onDismissError = {
            if (uiState.tour == null) {
                viewModel.onEvent(TourBookingConfirmEvent.OnBackClick)
                onBackClick()
            } else {
                viewModel.onEvent(TourBookingConfirmEvent.OnDismissError)
            }
        },
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
    onDismissError: () -> Unit = onBackClick,
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
            onRetryClick = onRetryClick,
            onDismissError = onDismissError
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
