package com.softserveacademy.home.presentation.ui.components.detailsScreenComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.components.TravelGalleryCarousel
import com.softserveacademy.core.presentation.design_system.components.TravelRatingBar
import com.softserveacademy.core.presentation.design_system.components.util.detailsScreenUtilities.TravelHotelDetailsTopIcons
import com.softserveacademy.core.presentation.design_system.theme.BlueDark90_Alpha50
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * Displays the header section of the detail screen, including the image carousel
 * and basic info.
 *
 * @param imageList List of image URLs to show in the carousel.
 * @param name The name of the destination.
 * @param rating The destination's user rating.
 * @param limitedReviews Formatted string representing the number of reviews.
 * @param onBackClick Action to perform when the back button is clicked.
 * @param onSeeAllPhotosClick Action to perform when the images button is clicked.
 * @param onShareClick Action to perform when the share button is clicked.
 * @param onFavoriteClick Action to perform when the favorite button is clicked.
 */
@Composable
fun TravelDetailsHeader(
    imageList : List<String>,
    name : String,
    rating : Double,
    limitedReviews : String,
    onBackClick: () -> Unit,
    onSeeAllPhotosClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {

        TravelGalleryCarousel(
            images = imageList.take(5),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        TravelHotelDetailsTopIcons(onBackClick,onShareClick,onFavoriteClick)

        Column(
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    start = TravelinDimens.PaddingMedium,
                    end = TravelinDimens.PaddingMedium,
                    bottom = TravelinDimens.Padding2ExtraLarge
                )
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {

                TravelRatingBar(rating = rating)

                Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))

                Text(
                    text = limitedReviews,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        if (imageList.size > 1) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        start = TravelinDimens.PaddingMedium,
                        end = TravelinDimens.PaddingMedium,
                        bottom = TravelinDimens.Padding2ExtraLarge
                    )
                    .clickable { onSeeAllPhotosClick() },
                color = BlueDark90_Alpha50,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = stringResource(id = R.string.see_all_label) + " " + stringResource(id = R.string.plus_photos_label).lowercase(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(
                            horizontal = TravelinDimens.PaddingSmall,
                            vertical = TravelinDimens.PaddingExtraSmall
                        ),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}