package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.core.domain.model.HotelRoomAmenity
import com.softserveacademy.core.presentation.design_system.components.util.displayName
import com.softserveacademy.core.presentation.design_system.components.util.icon
import com.softserveacademy.core.presentation.design_system.theme.BedIcon
import com.softserveacademy.core.presentation.design_system.theme.PersonsIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * A card component that displays detailed information about a hotel room.
 *
 * @param room The [HotelRoom] data to display.
 * @param nightCount The number of nights to calculate the total price.
 * @param isSelected Whether the card should be highlighted as selected.
 * @param onRoomSelected Callback when the card is clicked.
 */

@Composable
fun TravelHotelRoomCard(
    room: HotelRoom,
    nightCount: Int = 1,
    isSelected: Boolean = false,
    isClickable: Boolean = true,
    onRoomSelected: (HotelRoom) -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isSelected) Modifier.border(
                    3.dp,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.shapes.medium
                )
                else Modifier
            )
            .clickable(enabled = isClickable) { onRoomSelected(room) },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = TravelinDimens.ElevationSmall
        )
    ) {
        Column {
            // Room Image Carousel
            TravelGalleryCarousel(
                images = room.images,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            Column(
                modifier = Modifier
                    .padding(TravelinDimens.PaddingMedium)
            ) {
                // Room Type Title
                Text(
                    text = room.type,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )

                // Room Description
                Text(
                    text = room.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = TravelinDimens.PaddingExtraSmall)
                )

                // Amenities / Details Grid
                FlowRow(
                    modifier = Modifier
                        .padding(top = TravelinDimens.PaddingMedium)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall),
                    verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
                ) {
                    TravelAmenityChip(
                        icon = PersonsIcon,
                        text = room.maxOccupancy,
                        contentColor = MaterialTheme.colorScheme.secondary,
                        outlineColor = MaterialTheme.colorScheme.secondary
                    )
                    TravelAmenityChip(
                        icon = BedIcon,
                        text = room.bedType,
                        contentColor = MaterialTheme.colorScheme.secondary,
                        outlineColor = MaterialTheme.colorScheme.secondary
                    )
                    room.amenities.forEach { amenity ->
                        TravelAmenityChip(
                            icon = amenity.icon,
                            text = amenity.displayName
                        )
                    }
                }
                // Price and Availability
                Row(
                    modifier = Modifier
                        .padding(top = TravelinDimens.PaddingLarge)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)
                    ) {
                        Text( // Per night price
                            text = "$${room.pricePerNight}/night",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            // Total price
                            text = "Total $${room.pricePerNight * nightCount}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    Text( // Availability badge
                        text = if (room.isAvailable) "Available" else "Unavailable",
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.extraLarge)
                            .background(
                                if (room.isAvailable) {
                                    MaterialTheme.colorScheme.secondary
                                } else {
                                    MaterialTheme.colorScheme.error
                                }
                            )
                            .padding(
                                horizontal = TravelinDimens.PaddingMedium,
                                vertical = TravelinDimens.PaddingSmall
                            ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelHotelRoomCardPreview() {
    val sampleRoom = HotelRoom(
        type = "Deluxe Suite, King Size Bed",
        description = "Spacious suite with a king-size bed and a private balcony.",
        maxOccupancy = "1-5 persons",
        bedType = "1 King bed",
        amenities = listOf(
            HotelRoomAmenity.WIFI,
            HotelRoomAmenity.BREAKFAST,
            HotelRoomAmenity.PARKING,
            HotelRoomAmenity.AC,
            HotelRoomAmenity.ROOM_SERVICE
        ),
        pricePerNight = 150,
        images = listOf(
            "https://picsum.photos/200/300",
            "https://picsum.photos/200"
        ),
        isAvailable = true
    )

    Travelin2026ProjectLabTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TravelHotelRoomCard(
                room = sampleRoom,
                nightCount = 3,
                isSelected = false
            )
            TravelHotelRoomCard(
                room = sampleRoom.copy(
                    type = "Standard Suite, Queen Size Bed",
                    description = "Cozy suite with a queen-size bed and a shared bathroom.",
                    maxOccupancy = "1-4 persons",
                    bedType = "1 Queen bed",
                ),
                nightCount = 3,
                isSelected = true
            )
        }
    }
}
