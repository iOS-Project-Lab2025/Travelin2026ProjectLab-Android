package com.softserveacademy.feature.booking.hotel.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.presentation.design_system.components.TravelHotelRoomCard
import com.softserveacademy.feature.booking.hotel.presentation.events.HotelBookingConfirmEvent
import com.softserveacademy.feature.booking.hotel.presentation.states.HotelBookingConfirmState
import com.softserveacademy.feature.booking.hotel.presentation.ui.components.HotelBookingSummaryCard
import com.softserveacademy.feature.booking.hotel.presentation.ui.components.HotelSummaryCard
import com.softserveacademy.feature.booking.hotel.presentation.viewmodel.HotelBookingConfirmViewModel
import com.softserveacademy.core.presentation.design_system.theme.ArrowLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.core.domain.model.HotelDetails
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.feature.booking.hotel.domain.model.HotelBookingDraft
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.feature.booking.common.presentation.ui.screens.TravelBookingLoadingScreen
import com.softserveacademy.feature.booking.hotel.domain.model.ContactInfo
import com.softserveacademy.feature.booking.common.presentation.R as CommonR
import com.softserveacademy.feature.booking.hotel.presentation.R

@Composable
fun HotelBookingConfirmScreen(
    onBackClick: () -> Unit,
    viewModel: HotelBookingConfirmViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    HotelBookingConfirmContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onConfirmClick = { viewModel.onEvent(HotelBookingConfirmEvent.OnConfirmClick) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelBookingConfirmContent(
    uiState: HotelBookingConfirmState,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.booking_confirm_title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(TravelinDimens.PaddingMedium)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = ArrowLeftIcon, contentDescription = stringResource(CommonR.string.back_button_label))
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .padding(TravelinDimens.PaddingMedium)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val totalPrice = uiState.selectedRoom?.let { room ->
                        val nights = uiState.bookingDraft?.let { draft ->
                            val checkIn = draft.checkIn
                            val checkOut = draft.checkOut
                            if (checkIn != null && checkOut != null) {
                                ((checkOut - checkIn) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(1)
                            } else 1
                        } ?: 1
                        room.pricePerNight * nights
                    } ?: 0
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            ) {
                                append(stringResource(R.string.booking_confirm_total_label))
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                )
                            ) {
                                append("$$totalPrice")
                            }
                        },
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.weight(1f)
                    )
                    TravelPrimaryButton(
                        text = stringResource(R.string.booking_confirm_button_label),
                        onClick = onConfirmClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            TravelBookingLoadingScreen()
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.error ?: "Unknown error")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(TravelinDimens.PaddingMedium)
            ) {
                uiState.hotelDetails?.let { hotel ->
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
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = TravelinDimens.ElevationSmall
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(TravelinDimens.PaddingMedium)
                        ) {
                            // Contact Information
                            Text(
                                text = stringResource(R.string.contact_info_title),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

                            // First Name
                            ContactField(
                                label = stringResource(R.string.contact_info_first_name),
                                value = uiState.bookingDraft?.contactInfo?.firstName ?: ""
                            )
                            Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

                            // Last Name
                            ContactField(
                                label = stringResource(R.string.contact_info_last_name),
                                value = uiState.bookingDraft?.contactInfo?.lastName ?: ""
                            )
                            Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

                            // Phone
                            ContactField(
                                label = stringResource(R.string.contact_info_phone),
                                value = uiState.bookingDraft?.contactInfo?.phoneNumber ?: ""
                            )
                            Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

                            // Email
                            ContactField(
                                label = stringResource(R.string.contact_info_email),
                                value = uiState.bookingDraft?.contactInfo?.email ?: ""
                            )
                            Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
                        }
                    }
                }
            }
        }
    }
}


/**
 * Composable to display a contact field with a label and value (Read-only).
 *
 * @param label The label text for the contact field.
 * @param value The value text for the contact field.
 */
@Composable
fun ContactField(
    label: String,
    value: String
) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    )
}


@Preview(showBackground = true)
@Composable
fun HotelBookingConfirmPreview() {
    val sampleHotel = HotelDetails(
        id = 1,
        name = "Swiss-Belhotel Rainforest Kuta",
        address = "Jl. Sunset Road No. 101, Kuta, Bali, Indonesia",
        star = 5,
        rating = 4.9,
        numberOfReviews = 100,
        image = listOf("https://picsum.photos/800/600"),
        imageList = listOf("https://picsum.photos/800/600"),
        minimumPrice = 150,
        description = "A beautiful hotel in Kuta."
    )

    val sampleRoom = HotelRoom(
        id = 1,
        type = "Standard Suite, Queen Size Bed",
        description = "Volcano in East Java",
        maxOccupancy = "1-5 persons",
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
            phoneNumber = "123 456 789"
        )
    )

    Travelin2026ProjectLabTheme() {
        HotelBookingConfirmContent(
            uiState = HotelBookingConfirmState(
                hotelDetails = sampleHotel,
                selectedRoom = sampleRoom,
                bookingDraft = sampleDraft
            ),
            onBackClick = {},
            onConfirmClick = {}
        )
    }
}
