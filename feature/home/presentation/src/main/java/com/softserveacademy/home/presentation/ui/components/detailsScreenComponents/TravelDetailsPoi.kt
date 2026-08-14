package com.softserveacademy.home.presentation.ui.components.detailsScreenComponents

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import com.softserveacademy.core.domain.model.Poi
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.components.TravelOutlinedButton
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelArrowIconButton
import com.softserveacademy.core.presentation.design_system.theme.LocationMarkerIcon
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.TravelImageHandler
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelIcon
import com.softserveacademy.core.presentation.design_system.theme.BusIcon
import com.softserveacademy.core.presentation.design_system.theme.CarIcon
import com.softserveacademy.core.presentation.design_system.theme.PlaneIcon
import com.softserveacademy.core.presentation.design_system.theme.RestaurantIcon
import com.softserveacademy.core.presentation.design_system.theme.TrainIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme

/**
 * Displays a section showing important or touristic places near the destination.
 *
 * @param nearbyPlaces List of nearby places to display.
 * @param onSeeMoreClick Callback when the "See more" button is clicked.
 */
@Composable
fun NearbyPlacesSection(
    nearbyPlaces: List<Poi>,
    onSeeMoreClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(horizontal = TravelinDimens.PaddingLarge)
    ) {
        Text(
            text = stringResource(id = R.string.nearby_places_label),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryFixed,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        nearbyPlaces.take(5).forEach { place ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = TravelinDimens.PaddingSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TravelIcon(LocationMarkerIcon)

                    Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))

                    Column {
                        Text(
                            text = place.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = place.type,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Text(
                    text = place.travelTime,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }


        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        TravelOutlinedButton(
            text = stringResource(id = R.string.see_more_nearby_places),
            onClick = onSeeMoreClick,
            contentPadding = PaddingValues(
                horizontal = TravelinDimens.PaddingLarge,
                vertical = TravelinDimens.PaddingSmall
            )
        )

    }
}

/**
 * Full-screen overlay displaying detailed "Explore the area" information.
 *
 * @param nearbyPlaces List of nearby places.
 * @param areaDescription Description of the area.
 * @param nearbyTransport List of nearby transport options.
 * @param nearbyRestaurants List of nearby restaurants.
 * @param onDismiss Callback to close the overlay.
 */
@Composable
fun ExploreAreaOverlay(
    nearbyPlaces: List<Poi>,
    areaDescription: String?,
    nearbyTransport: List<Poi> = emptyList(),
    nearbyRestaurants: List<Poi> = emptyList(),
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = TravelinDimens.PaddingLarge)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = TravelinDimens.PaddingSmall),
                contentAlignment = Alignment.Center
            ) {
                TravelArrowIconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                Text(
                    text = stringResource(id = R.string.explore_area_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceLarge)
            ) {
                if (nearbyPlaces.isNotEmpty()) {
                    item {
                        PoiImages(nearbyPlaces)
                    }
                }

                if (areaDescription != null) {
                    item {
                        DescriptionAreaSection(areaDescription)
                    }
                }

                if (nearbyPlaces.isNotEmpty()) {
                    item {
                        NearbyPoiSection(nearbyPlaces)
                    }
                }

                if (nearbyTransport.isNotEmpty()) {
                    item {
                        GettingAroundSection(nearbyTransport)
                    }
                }

                if (nearbyRestaurants.isNotEmpty()) {
                    item {
                        NearbyRestaurantsList(nearbyRestaurants)
                    }
                }
            }
        }
    }
}
/**
 * Displays a grid of images for nearby places.
 *
 * @param nearbyPlaces List of nearby places to display.
 */
@Composable
private fun PoiImages(
    nearbyPlaces : List<Poi>
){
    Column {
        TravelImageHandler(
            image = nearbyPlaces[0].imageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .height(TravelinDimens.ImageSizeExtraLarge,)
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        Text(
            text = nearbyPlaces[0].name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = nearbyPlaces[0].travelTime,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

    val gridPlaces = nearbyPlaces.drop(1).take(4)

    Column(verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)) {
        gridPlaces.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
            ) {
                rowItems.forEach { place ->
                    Column(modifier = Modifier.weight(1f)) {

                        TravelImageHandler(
                            image = place.imageUrl,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(TravelinDimens.ImageSizeMedium,)
                        )

                        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))

                        Text(
                            text = place.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = place.travelTime,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * Displays the description of the area.
 *
 * @param areaDescription Description of the area.
 */
@Composable
private fun DescriptionAreaSection(
    areaDescription : String
) {
    Column {
        Text(
            text = stringResource(id = R.string.about_area_label),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
        Text(
            text = areaDescription,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Displays a list of nearby points of interest.
 *
 * @param nearbyPlaces List of nearby places.
 */
@Composable
private fun NearbyPoiSection(
    nearbyPlaces : List<Poi>
){
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {

            TravelIcon(LocationMarkerIcon)

            Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))

            Text(
                text = stringResource(id = R.string.whats_nearby_label),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        nearbyPlaces.drop(5).take(5).forEach { place ->
            Text(
                text = "${place.name} - ${place.travelTime}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(
                        vertical = TravelinDimens.SpaceExtraSmall,
                        horizontal = TravelinDimens.SpaceSmall + TravelinDimens.IconSizeSmall
                    )
            )
        }
    }
}

/**
 * Displays transport options in the area.
 *
 * @param nearbyTransport List of nearby transport options.
 */
@Composable
private fun GettingAroundSection(
    nearbyTransport : List<Poi>
){
    Column{
        Row(verticalAlignment = Alignment.CenterVertically) {

            TravelIcon(CarIcon)

            Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))

            Text(
                text = stringResource(id = R.string.getting_around_label),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        nearbyTransport.forEach { transport ->
            val icon = when {
                transport.type.contains("bus", true) -> BusIcon
                transport.type.contains("train", true) || transport.type.contains("transit", true) -> TrainIcon
                transport.type.contains("airport", true) -> PlaneIcon
                else -> BusIcon
            }
            TransportItem(
                icon = icon,
                name = transport.name,
                time = transport.travelTime
            )
        }
    }
}
/**
 * Displays a list of nearby restaurants.
 *
 * @param nearbyRestaurants List of nearby restaurants.
 */
@Composable
private fun NearbyRestaurantsList(
    nearbyRestaurants : List<Poi>
){
    Column(
        modifier = Modifier
        .padding(
            bottom = TravelinDimens.PaddingLarge
        ))
    {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TravelIcon(RestaurantIcon)

            Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))
            Text(
                text = stringResource(id = R.string.nearby_restaurants_label),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        nearbyRestaurants.forEach { restaurant ->
            Text(
                text = "${restaurant.name} - ${restaurant.travelTime}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(
                        vertical = TravelinDimens.SpaceExtraSmall,
                        horizontal = TravelinDimens.SpaceSmall + TravelinDimens.IconSizeSmall
                    )
            )
        }
    }
}


/**
 * Displays an individual transport item with an icon and name.
 *
 * @param icon Icon for the transport type.
 * @param name Name of the transport hub.
 * @param time Travel time.
 */
@Composable
private fun TransportItem(icon: ImageVector, name: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = TravelinDimens.SpaceExtraSmall,
                horizontal = TravelinDimens.SpaceSmall + TravelinDimens.IconSizeSmall
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TravelIcon(icon)

        Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))

        Text(text = "$name - $time", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/**
 * Preview for [NearbyPlacesSection].
 */
@Preview(showBackground = true)
@Composable
private fun NearbyPlacesSectionPreview() {
    Travelin2026ProjectLabTheme {
        NearbyPlacesSection(
            nearbyPlaces = listOf(
                Poi("National Museum", "Museum", "5 min walk"),
                Poi("Central Park", "Park", "10 min walk"),
                Poi("Old Town Square", "Historical Plaza", "15 min walk")
            ),
            onSeeMoreClick = {}
        )
    }
}

/**
 * Preview for [ExploreAreaOverlay].
 */
@Preview(showBackground = true)
@Composable
private fun ExploreAreaOverlayPreview() {
    Travelin2026ProjectLabTheme {
        ExploreAreaOverlay(
            nearbyPlaces = listOf(
                Poi("National Museum", "Museum", "5 min walk"),
                Poi("Central Park", "Park", "10 min walk"),
                Poi("Old Town Square", "Historical Plaza", "15 min walk")
            ),
            areaDescription = "This area is known for its beautiful architecture and vibrant street life. It is the heart of the city's cultural scene.",
            nearbyTransport = listOf(
                Poi("Central Station", "train", "8 min"),
                Poi("Main Bus Terminal", "bus", "5 min")
            ),
            nearbyRestaurants = listOf(
                Poi("The Gourmet Kitchen", "Restaurant", "3 min walk"),
                Poi("Coffee Corner", "Cafe", "2 min walk")
            ),
            onDismiss = {}
        )
    }
}

