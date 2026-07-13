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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.theme.StarIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * A reusable card for displaying hotel information.
 *
 * @param hotel The hotel object containing information needed to display in the card.
 *
 * @see Hotel
 */
@Composable
fun TravelCardHorizontal(hotel: Hotel){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(TravelinDimens.ImageSizeMedium)
                .background(MaterialTheme.colorScheme.background)
                .clip(MaterialTheme.shapes.medium)
        ) {
            // Add image and modify his size.
            TravelImageHandler(
                image = hotel.image,
                imageWidth = TravelinDimens.ImageSizeMedium,
                imageHeight = TravelinDimens.ImageSizeMedium,
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
                    text = hotel.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineSmall,
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
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        top = TravelinDimens.PaddingSmall
                    )
                ) {
                    Icon(
                        imageVector = StarIcon,
                        contentDescription = "hotel star",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(TravelinDimens.IconSizeExtraSmall)
                            .align(Alignment.CenterVertically)
                    )

                    Text(
                        text = "${hotel.star}-star hotel",
                        modifier = Modifier.padding(
                            start = TravelinDimens.PaddingExtraSmall
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = "$ ${hotel.pricePerNight}/night",
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
    val hotelExample = Hotel(
        name = "Swiss-Belhotel Rainforest Kuta",
        address = "Jl. Sunset Road No. 101, Kuta, Bali , Indonesia",
        star = 4,
        pricePerNight = 50,
        image = R.drawable.test_hotel,
        imagesList = emptyList()
    )
    Travelin2026ProjectLabTheme(darkTheme = true) {
        TravelCardHorizontal(hotel = hotelExample)
    }
}
