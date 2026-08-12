package com.softserveacademy.feature.booking.hotel.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.presentation.design_system.components.TravelHotelRoomCard
import com.softserveacademy.feature.booking.hotel.presentation.events.HotelBookingConfirmEvent
import com.softserveacademy.feature.booking.hotel.presentation.states.HotelBookingConfirmState
import com.softserveacademy.feature.booking.hotel.presentation.ui.components.HotelBookingSummaryCard
import com.softserveacademy.feature.booking.hotel.presentation.ui.components.HotelSummaryCard
import com.softserveacademy.feature.booking.common.presentation.ui.components.TravelPaymentSimulationSheet
import com.softserveacademy.feature.booking.hotel.presentation.viewmodel.HotelBookingConfirmViewModel
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.feature.booking.hotel.domain.model.HotelBookingDraft
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.countries
import com.softserveacademy.core.presentation.design_system.components.TravelLoadingScreen
import com.softserveacademy.feature.booking.common.presentation.ui.components.TravelBookingContactInfoCard
import com.softserveacademy.feature.booking.hotel.domain.model.ContactInfo
import com.softserveacademy.feature.booking.hotel.presentation.R
import com.softserveacademy.core.presentation.design_system.R as coreR

import androidx.compose.runtime.LaunchedEffect
import com.softserveacademy.core.presentation.design_system.theme.BlueDark80
import com.softserveacademy.core.presentation.design_system.theme.Gray40
import com.softserveacademy.core.presentation.design_system.theme.Gray80
import com.softserveacademy.core.presentation.design_system.theme.GrayLight20
import com.softserveacademy.core.presentation.design_system.theme.Red50
import com.softserveacademy.core.presentation.design_system.theme.Teal40
import com.softserveacademy.core.presentation.design_system.theme.White100
import com.softserveacademy.feature.booking.common.presentation.ui.screens.TravelBookingConfirmScreen
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet

@Composable
fun HotelBookingConfirmScreen(
    onBackClick: () -> Unit,
    onPaymentSuccess: () -> Unit,
    viewModel: HotelBookingConfirmViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val paymentSheet = rememberPaymentSheet { result ->
        when (result) {
            is PaymentSheetResult.Completed -> {
                viewModel.onEvent(HotelBookingConfirmEvent.OnPaymentSuccess)
            }
            is PaymentSheetResult.Canceled -> {
                viewModel.onEvent(HotelBookingConfirmEvent.OnPaymentReset)
            }
            is PaymentSheetResult.Failed -> {
                viewModel.onEvent(HotelBookingConfirmEvent.OnPaymentReset)
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
            viewModel.onEvent(HotelBookingConfirmEvent.OnPaymentReset)
        }
    }

    LaunchedEffect(uiState.isPaymentSuccessful) {
        if (uiState.isPaymentSuccessful) {
            onPaymentSuccess()
        }
    }

    HotelBookingConfirmContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onConfirmClick = { viewModel.onEvent(HotelBookingConfirmEvent.OnConfirmClick) },
        onRetryClick = { viewModel.onEvent(HotelBookingConfirmEvent.OnRetryClick) },
        onDismissError = {
            if (uiState.hotel == null) {
                onBackClick()
            } else {
                viewModel.onEvent(HotelBookingConfirmEvent.OnDismissError)
            }
        },
        onSimulateSuccess = { viewModel.onEvent(HotelBookingConfirmEvent.OnSimulateSuccessClick) },
        onSimulateFailure = { viewModel.onEvent(HotelBookingConfirmEvent.OnSimulateFailureClick) },
        onDismissBottomSheet = { viewModel.onEvent(HotelBookingConfirmEvent.OnDismissPaymentSimulationSheet) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelBookingConfirmContent(
    uiState: HotelBookingConfirmState,
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
            totalPrice = uiState.totalPrice,
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
                uiState.hotel?.let { hotel ->
                    HotelSummaryCard(hotel = hotel)

                    Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

                    // Booking Details Section
                    HotelBookingSummaryCard(
                        checkIn = uiState.bookingDraft?.checkIn ?: 0L,
                        checkOut = uiState.bookingDraft?.checkOut ?: 0L,
                        guests = uiState.bookingDraft?.guests?.let {
                            stringResource(
                                R.string.booking_confirm_guests_format,
                                it.adults,
                                it.children,
                                stringResource(if (it.pets) R.string.booking_confirm_pets_yes else R.string.booking_confirm_pets_no)
                            )
                        } ?: ""
                    )

                    Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

                    // Room Selection
                    uiState.selectedRoom?.let { room ->
                        val nights = uiState.bookingDraft?.let { draft ->
                            val checkIn = draft.checkIn
                            val checkOut = draft.checkOut
                            if (checkIn != null && checkOut != null) {
                                ((checkOut - checkIn) / (1000 * 60 * 60 * 24)).toInt()
                                    .coerceAtLeast(1)
                            } else 1
                        } ?: 1
                        TravelHotelRoomCard(
                            room = room,
                            nightCount = nights,
                            isSelected = false,
                            isClickable = false
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
                        subtitle = stringResource(R.string.contact_info_who_check_in)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HotelBookingConfirmPreview() {
    val sampleHotel = Hotel(
        id = "1",
        name = "Swiss-Belhotel Rainforest Kuta",
        address = "Jl. Sunset Road No. 101, Kuta, Bali, Indonesia",
        starCategory = 5,
        reviewRating = 4.9,
        numberOfReviews = 100,
        imageList = listOf("https://picsum.photos/800/600"),
        description = "A beautiful hotel in Kuta.",
        latitude = 1.35,
        longitude = 103.87,
    )

    val sampleRoom = HotelRoom(
        id = "1",
        type = "Standard Suite, Queen Size Bed",
        description = "Volcano in East Java",
        maxOccupancy = 5,
        bedType = "1 Queen bed",
        amenities = emptyList(),
        pricePerNight = 150,
        images = listOf("https://picsum.photos/400/300")
    )

    val sampleDraft = HotelBookingDraft(
        hotelId = "1",
        roomId = "1",
        checkIn = 1782115200000L, // Thursday, July 23, 2026
        checkOut = 1782374400000L, // Sunday, July 26, 2026
        contactInfo = ContactInfo(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@gmail.com",
            countryCode = "+1",
            phoneNumber = "123 456 789"
        )
    )

    Travelin2026ProjectLabTheme {
        HotelBookingConfirmContent(
            uiState = HotelBookingConfirmState(
                hotel = sampleHotel,
                selectedRoom = sampleRoom,
                bookingDraft = sampleDraft
            ),
            onBackClick = {},
            onConfirmClick = {},
            onRetryClick = {}
        )
    }
}
