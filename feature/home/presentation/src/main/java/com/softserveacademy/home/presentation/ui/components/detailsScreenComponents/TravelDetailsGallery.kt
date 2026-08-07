package com.softserveacademy.home.presentation.ui.components.detailsScreenComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.components.TravelOutlinedButton
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * Displays a preview of the hotel gallery with 3 images and a button to view all photos.
 *
 * @param imageList List of image URLs to choose from.
 * @param numberOfImages Total number of images available.
 * @param onSeeAllPhotosClick Action to perform when the button is clicked.
 */
@Composable
fun TravelDetailsGallery(
    imageList: List<String>,
    numberOfImages: Int,
    onSeeAllPhotosClick: () -> Unit
) {
    if (imageList.isEmpty()) return

    Column(
        modifier = Modifier
            .padding(horizontal = TravelinDimens.PaddingLarge)
            .padding(bottom = TravelinDimens.PaddingLarge)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
            ) {
                if (imageList.isNotEmpty()) {
                    AsyncImage(
                        model = imageList[0],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.test_hotel),
                        error = painterResource(R.drawable.test_hotel)
                    )
                }
                if (imageList.size > 1) {
                    AsyncImage(
                        model = imageList[1],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.test_hotel),
                        error = painterResource(R.drawable.test_hotel)
                    )
                }
            }

            if (imageList.size > 2) {
                AsyncImage(
                    model = imageList[2],
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.test_hotel),
                    error = painterResource(R.drawable.test_hotel)
                )
            }
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

        TravelOutlinedButton(
            text = "${stringResource(id = R.string.see_all_label)} ${numberOfImages - 1} " +
                    stringResource(id = R.string.plus_photos_label),
            onClick = onSeeAllPhotosClick,
            contentPadding = PaddingValues(
                horizontal = TravelinDimens.PaddingLarge,
                vertical = TravelinDimens.PaddingSmall
            )
        )
    }
}