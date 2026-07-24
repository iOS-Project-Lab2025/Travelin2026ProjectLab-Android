package com.softserveacademy.feature.booking.hotel.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.softserveacademy.core.domain.model.HotelDetails
import com.softserveacademy.core.presentation.design_system.components.TravelRatingBar
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelStar
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * A card component that displays a summary of a hotel, including its image, name, address,
 * star rating, and user reviews.
 *
 * @param hotel The [HotelDetails] object containing the hotel information to display.
 * @param modifier The modifier to be applied to the card.
 */
@Composable
fun HotelSummaryCard(
    hotel: HotelDetails,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = TravelinDimens.ElevationSmall
        )
    ) {
        Column {
            AsyncImage(
                model = hotel.image.firstOrNull(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TravelinDimens.ImageSizeLarge)
                    .clip(MaterialTheme.shapes.large),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(TravelinDimens.PaddingMedium)
            ) {
                Text(
                    text = hotel.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = hotel.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = TravelinDimens.PaddingExtraSmall)
                )

                Row(
                    modifier = Modifier.padding(vertical = TravelinDimens.PaddingSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TravelRatingBar(rating = hotel.star.toDouble())
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TravelStar(starSize = TravelinDimens.IconSizeSmall)
                    Spacer(modifier = Modifier.width(TravelinDimens.SpaceExtraSmall))
                    Text(
                        text = "${hotel.rating} (${hotel.numberOfReviews} reviews)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HotelSummaryCardPreview() {
    Travelin2026ProjectLabTheme() {
        HotelSummaryCard(
            hotel = HotelDetails(
                id = 1,
                name = "Swiss-Belhotel Rainforest Kuta",
                address = "Jl. Sunset Road No. 101, Kuta, Bali, Indonesia",
                star = 3,
                rating = 4.9,
                numberOfReviews = 100,
                image = listOf("https://picsum.photos/800/600"),
                imageList = listOf("https://picsum.photos/800/600"),
                minimumPrice = 150,
                description = "A beautiful hotel in Kuta.",
                latitude = 1.35,
                longitude = 103.87,
            )
        )
    }
}
