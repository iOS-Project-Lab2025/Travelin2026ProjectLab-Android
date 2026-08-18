package com.softserveacademy.home.presentation.ui.components.detailsScreenComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.shimmerEffect
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * Skeleton loader for the Hotel Detail Screen.
 *
 * This component provides a shimmering placeholder UI that mimics the layout of the
 * hotel detail screen while data is being loaded.
 *
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun TravelPoiLoading(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

            repeat(6) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(TravelinDimens.IconSizeLarge)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun HotelDetailLoadingPreview(){
    Travelin2026ProjectLabTheme(darkTheme = true){
        TravelPoiLoading()
    }
}