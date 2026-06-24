package com.softserveacademy.core.presentation.design_system.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.domain.Hotel
import com.softserveacademy.core.presentation.design_system.Green50
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.Yellow50
import com.softserveacademy.core.presentation.design_system.theme.StarIcon
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.core.presentation.design_system.theme.shapes

@Composable
fun AppCardHorizontal(hotel: Hotel){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(TravelinDimens.ImageSizeMedium)
        ) {
            // Add image and modify his size.
            HotelImage(
                image = hotel.image,
                imageWidth = TravelinDimens.ImageSizeMedium,
                imageHeight = TravelinDimens.ImageSizeMedium,
                shape = shapes.medium
            )

            Column(
                modifier = Modifier
                    .padding(TravelinDimens.PaddingSmall)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = hotel.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    // Todo: edit style and color.
                    style = MaterialTheme.typography.titleMedium,
                    color = Green50
                )
                Text(
                    text = hotel.address,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = TravelinDimens.PaddingExtraSmall),
                    // Todo: edit style and color.
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = TravelinDimens.PaddingSmall)
                ) {
                    // Todo: change for call of star icon.
                    Icon(
                        imageVector = StarIcon,
                        contentDescription = "hotel star",
                        tint = Yellow50,
                        modifier = Modifier.size(TravelinDimens.IconSizeExtraSmall)
                    )

                    Spacer(modifier = Modifier.width(TravelinDimens.SpaceExtraSmall))

                    Text(
                        text = "${hotel.star}-star hotel"
                    )
                }

                Text(
                    text = "$ ${hotel.pricePerNight}/night",
                    modifier = Modifier.align(Alignment.End),
                    // Todo: edit color.
                    color = Green50
                )
            }
        }
    }



@Preview(showBackground = false)
@Composable
fun HotelCardHorizontalPreview() {
    val hotelExample = Hotel(
        name = "Swiss-Belhotel Rainforest Kuta",
        address = "Jl. Sunset Road No. 101, Kuta, Bali , Indonesia",
        star = 4,
        pricePerNight = 50,
        image = R.drawable.mask_group
    )
    AppCardHorizontal(hotel = hotelExample)
}