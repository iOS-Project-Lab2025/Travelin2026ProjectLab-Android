package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.presentation.design_system.theme.StarIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * A reusable card for displaying information about a travel package.
 */
@Composable
fun TravelCardVertical(
    title: String,
    location: String,
    rating: String,
    price: String,
    duration: String,
    imageUrl: String,
    onClick: () -> Unit = {}
){
    Column(
        modifier = Modifier
            .height(240.dp)
            .width(180.dp)
            .shadow(elevation = TravelinDimens.ElevationMedium, shape = MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.background)
            .clickable { onClick() }
    ) {
        TravelImageHandler(
            image = imageUrl,
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
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = location,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    top = TravelinDimens.PaddingExtraSmall
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = TravelinDimens.PaddingSmall
                    )
            ) {
                Icon(
                    imageVector = StarIcon,
                    contentDescription = "rating star",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(TravelinDimens.IconSizeExtraSmall)
                )

                Text(
                    text = rating,
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
                        text = price,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.width(TravelinDimens.SpaceExtraSmall))

                Text(
                    text = duration,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraLarge)
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
    Travelin2026ProjectLabTheme(darkTheme = false) {
        TravelCardVertical(
            title = "Mount Bromo",
            location = "Volcano in East Java",
            rating = "4.9",
            price = "$ 150/pax",
            duration = "3D2N",
            imageUrl = "https://picsum.photos/200"
        )
    }
}
