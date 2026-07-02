package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.theme.StarIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.core.presentation.design_system.theme.shapes

/**
 * A reusable card for displaying information about the tour package.
 *
 * @param hotel The hotel object containing information needed to display in the card.
 *
 * @see Hotel
 */
@Composable
fun TravelCardVertical(hotel: Hotel){
    Column(
        modifier = Modifier
            .height(240.dp)
            .width(180.dp)
            .clip(shapes.medium)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Add image and modify his size.
        TravelImageHandler(
            image = hotel.image,
            imageWidth = 180.dp,
            imageHeight = TravelinDimens.ImageSizeLarge,
            shouldClip = false
        )
        Column(
            modifier = Modifier
                .padding(TravelinDimens.PaddingSmall)
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Text(
                text = hotel.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = hotel.address,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    top = TravelinDimens.PaddingExtraSmall
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                //verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = TravelinDimens.PaddingSmall
                    )
            ) {
                Icon(
                    imageVector = StarIcon,
                    contentDescription = "hotel star",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(TravelinDimens.IconSizeExtraSmall)
                )

                Text(
                    text = "${hotel.userRating}",
                    modifier = Modifier.padding(
                        start = TravelinDimens.PaddingExtraSmall
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(top = TravelinDimens.PaddingSmall)
                    .fillMaxWidth()
            ) {
                Column{
                    Text(
                        text = "Start from",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "$ ${hotel.pricePerNight}/pax",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.width(TravelinDimens.SpaceExtraSmall))

                Text(
                    text = "3D2N",
                    modifier = Modifier
                        .clip(shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(
                            horizontal = TravelinDimens.PaddingSmall,
                            vertical = TravelinDimens.Padding2ExtraSmall
                        ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}


@Preview(showBackground = false)
@Composable
fun TravelCardVerticalPreview() {
    val hotelExample = Hotel(
        name = "Mount Bromo",
        address = "Volcano in East Java",
        userRating = 4.9,
        pricePerNight = 150,
        image = R.drawable.test_place
    )
    Travelin2026ProjectLabTheme(darkTheme = false) {
        TravelCardVertical(hotel = hotelExample)
    }
}
