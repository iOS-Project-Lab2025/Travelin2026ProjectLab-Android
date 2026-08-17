package com.softserveacademy.feature.booking.tour.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.presentation.design_system.components.TravelFeatureChip
import com.softserveacademy.core.presentation.design_system.components.TravelRatingBar
import com.softserveacademy.core.presentation.design_system.components.util.mapToFeature
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelStar
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * A card component that displays a summary of a tour, including its image, title, location,
 * rating, and included services.
 *
 * @param tour The [Tour] object containing the tour information to display.
 * @param modifier The modifier to be applied to the card.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TourSummaryCard(
    tour: Tour,
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
                model = tour.imageList.firstOrNull(),
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
                    text = tour.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = tour.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = TravelinDimens.PaddingExtraSmall)
                )

                Row(
                    modifier = Modifier.padding(vertical = TravelinDimens.PaddingSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TravelRatingBar(rating = tour.rating)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TravelStar(starSize = TravelinDimens.IconSizeSmall)
                    Spacer(modifier = Modifier.width(TravelinDimens.SpaceExtraSmall))
                    Text(
                        text = "${tour.rating} (${tour.limitedReviews})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (tour.includedServices.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(TravelinDimens.SpaceSmall),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(TravelinDimens.SpaceSmall)
                    ) {
                        tour.includedServices.forEach { serviceId ->
                            mapToFeature(serviceId)?.let { feature ->
                                TravelFeatureChip(feature = feature)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TourSummaryCardPreview() {
    Travelin2026ProjectLabTheme {
        TourSummaryCard(
            tour = Tour(
                id = "1",
                title = "Bali Adventure Tour",
                location = "Bali, Indonesia",
                rating = 4.8,
                numberOfReviews = 150,
                imageList = listOf("https://picsum.photos/800/600"),
                includedServices = listOf("TOUR_TRANSPORT", "TOUR_GUIDE", "TOUR_BREAKFAST")
            )
        )
    }
}
