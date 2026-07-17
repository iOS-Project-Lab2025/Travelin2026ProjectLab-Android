package com.softserveacademy.feature.booking.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.core.domain.model.HotelRoomAmenity
import com.softserveacademy.core.presentation.design_system.components.TravelHotelRoomCard
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.presentation.components.TravelBookingBottomBar
import com.softserveacademy.feature.booking.presentation.components.TravelBookingLoadingScreen
import com.softserveacademy.feature.booking.presentation.components.TravelFilterChip

@Composable
fun HotelRoomSelectionScreen(
    onBackClick: () -> Unit,
    onRoomSelected: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HotelRoomSelectionViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    HotelRoomSelectionScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
        onNextClick = {
            viewModel.onEvent(HotelRoomSelectionEvent.OnNextClick)
            onRoomSelected()
        },
        modifier = modifier
    )
}

@Composable
fun HotelRoomSelectionScreenContent(
    state: HotelRoomSelectionState,
    onEvent: (HotelRoomSelectionEvent) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        TravelBookingLoadingScreen()
    } else {
        Scaffold(
            topBar = {
                Text(
                    text = stringResource(R.string.room_selection_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(TravelinDimens.PaddingMedium)
                )
            },
            bottomBar = {
                TravelBookingBottomBar(
                    onBackClick = onBackClick,
                    onNextClick = onNextClick,
                    nextButtonEnabled = state.selectedRoomId != null,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            modifier = modifier
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Filter Row
                FilterRow(
                    selectedFilter = state.selectedFilter,
                    onFilterSelected = { onEvent(HotelRoomSelectionEvent.OnFilterSelected(it)) }
                )

                if (state.filteredRooms.isEmpty()) {
                    EmptyRoomsState()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = TravelinDimens.PaddingMedium),
                        verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium),
                        contentPadding = PaddingValues(bottom = TravelinDimens.PaddingLarge)
                    ) {
                        items(state.filteredRooms) { room ->
                            TravelHotelRoomCard(
                                room = room,
                                nightCount = state.nightCount,
                                isSelected = state.selectedRoomId == room.id,
                                onRoomSelected = { onEvent(HotelRoomSelectionEvent.OnRoomSelected(it.id ?: 0)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    selectedFilter: RoomFilter,
    onFilterSelected: (RoomFilter) -> Unit
) {
    val filters = RoomFilter.entries
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = TravelinDimens.PaddingMedium),
        horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall),
        contentPadding = PaddingValues(horizontal = TravelinDimens.PaddingMedium)
    ) {
        items(filters) { filter ->
            TravelFilterChip(
                text = when (filter) {
                    RoomFilter.AVAILABLE -> stringResource(R.string.filter_available)
                    RoomFilter.ALL -> stringResource(R.string.filter_all)
                    RoomFilter.ONE_BED -> stringResource(R.string.filter_1_bed)
                    RoomFilter.TWO_BEDS -> stringResource(R.string.filter_2_beds)
                },
                isSelected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

@Composable
private fun EmptyRoomsState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.empty_rooms_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HotelRoomSelectionScreenPreview() {
    val mockRooms = listOf(
        HotelRoom(
            id = 1,
            type = "Deluxe Suite, King Size Bed",
            description = "Volcano in East Java",
            maxOccupancy = "1-5 persons",
            bedType = "1 King bed",
            bedCount = 1,
            amenities = listOf(HotelRoomAmenity.BREAKFAST, HotelRoomAmenity.WIFI),
            pricePerNight = 150,
            images = listOf(
                "https://picsum.photos/id/137/200/300",
                "https://picsum.photos/id/138/200/300"
            ),
            isAvailable = true
        ),
        HotelRoom(
            id = 2,
            type = "Standard Suite, Queen Size Bed",
            description = "Volcano in East Java",
            maxOccupancy = "1-5 persons",
            bedType = "1 Queen bed",
            bedCount = 1,
            amenities = listOf(HotelRoomAmenity.BREAKFAST, HotelRoomAmenity.WIFI),
            pricePerNight = 150,
            images = listOf(
                "https://picsum.photos/id/137/200/300",
                "https://picsum.photos/id/138/200/300"
            ),
            isAvailable = true
        )
    )
    val state = HotelRoomSelectionState(
        rooms = mockRooms,
        filteredRooms = mockRooms,
        selectedRoomId = 1,
        nightCount = 3
    )
    Travelin2026ProjectLabTheme {
        HotelRoomSelectionScreenContent(
            state = state,
            onEvent = {},
            onBackClick = {},
            onNextClick = {}
        )
    }
}
