package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.softserveacademy.core.presentation.design_system.theme.StarIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * A reusable card for displaying travel information (hotels, tours, etc.).
 *
 * @param title The title of the travel item.
 * @param address The location or address of the travel item.
 * @param starRating The star rating of the travel item.
 * @param ratingText Optional text to display next to the rating icon (e.g., "4-star hotel").
 * @param price The price to display.
 * @param priceSuffix Optional suffix for the price (e.g., "/night").
 * @param imageUrl The URL of the image to display.
 */
@Composable
fun TravelCardHorizontal(
    title: String,
    address: String,
    starRating: Double?,
    ratingText: String?,
    price: String,
    priceSuffix: String? = null,
    imageUrl: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(TravelinDimens.ImageSizeLarge)
            .shadow(elevation = TravelinDimens.ElevationMedium, shape = MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.background)
            .clip(MaterialTheme.shapes.medium)
    ) {
        TravelImageHandler(
            image = imageUrl,
            imageWidth = TravelinDimens.ImageSizeLarge,
            imageHeight = TravelinDimens.ImageSizeLarge,
            shape = MaterialTheme.shapes.medium
        )
        Column(
            modifier = Modifier
                .padding(
                    top = TravelinDimens.PaddingNormal,
                    bottom = TravelinDimens.PaddingSmall,
                    start = TravelinDimens.PaddingSmall,
                    end = TravelinDimens.PaddingSmall
                )
                .fillMaxHeight()
                .fillMaxWidth(),
        ) {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = address,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    top = TravelinDimens.PaddingExtraSmall
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    top = TravelinDimens.PaddingSmall
                )
            ) {
                if (starRating != null) {
                    Icon(
                        imageVector = StarIcon,
                        contentDescription = "rating star",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(TravelinDimens.IconSizeExtraSmall)
                            .align(Alignment.CenterVertically)
                    )

                    if (ratingText != null) {
                        Text(
                            text = ratingText,
                            modifier = Modifier.padding(
                                start = TravelinDimens.PaddingExtraSmall
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            Text(
                text = "$price${priceSuffix ?: ""}",
                modifier = Modifier.align(Alignment.End),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun TravelCardHorizontalPreview() {
    Travelin2026ProjectLabTheme(darkTheme = true) {
        TravelCardHorizontal(
            title = "Swiss-Belhotel Rainforest Kuta",
            address = "Jl. Sunset Road No. 101, Kuta, Bali , Indonesia",
            starRating = 4.0,
            ratingText = "4-star hotel",
            price = "$ 50",
            priceSuffix = "/night",
            imageUrl = "https://picsum.photos/200"
        )
    }
}
