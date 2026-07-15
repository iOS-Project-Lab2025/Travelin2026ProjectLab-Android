package com.softserveacademy.home.presentation.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.Gray40_Alpha20
import com.softserveacademy.core.presentation.design_system.theme.Gray40_Alpha60
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * A modifier that applies a shimmer effect to a composable.
 * Used for skeleton loading screens.
 */
fun Modifier.shimmerEffect(): Modifier = composed {

    val transition = rememberInfiniteTransition(label = "shimmer")

    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_animation"
    )

    val shimmerColors = listOf(
        Gray40_Alpha60,
        Gray40_Alpha20,
        Gray40_Alpha60
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    background(brush)
}

/**
 * Skeleton loader for the Hotel Detail Screen.
 *
 * This component provides a shimmering placeholder UI that mimics the layout of the
 * hotel detail screen while data is being loaded.
 *
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun HotelDetailLoading(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Header Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TravelinDimens.ImageSizeExtraLarge)
                    .shimmerEffect()
            )

            Column(
                modifier = Modifier
                    .padding(TravelinDimens.PaddingLarge)
            ) {
                // Title
                Box(
                    modifier = Modifier
                        .width(250.dp)
                        .height(TravelinDimens.SpaceExtraLarge)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect()
                )
                
                Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
                
                // Description
                repeat(6) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(TravelinDimens.PaddingMedium)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
                }

                Spacer(modifier = Modifier.height(TravelinDimens.Space2ExtraLarge))
                
                // What's included tittle
                Box(
                    modifier = Modifier
                        .width(TravelinDimens.ImageSizeLarge)
                        .height(TravelinDimens.IconSizeMedium)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect()
                        .padding(top = TravelinDimens.SpaceExtraLarge)
                )
                
                Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
                
                // Included items
                repeat(2) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(TravelinDimens.ButtonHeightMedium)
                                .clip(MaterialTheme.shapes.small)
                                .shimmerEffect()
                        )
                        Spacer(modifier = Modifier.width(TravelinDimens.SpaceMedium))
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(TravelinDimens.ButtonHeightMedium)
                                .clip(MaterialTheme.shapes.small)
                                .shimmerEffect()
                        )
                    }
                    Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

                }


            }

            // gallery
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = TravelinDimens.PaddingLarge,
                        vertical = TravelinDimens.PaddingMedium
                    )
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(TravelinDimens.ImageSizeLarge)
                        .clip(MaterialTheme.shapes.small)
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.width(TravelinDimens.SpaceMedium))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(TravelinDimens.ImageSizeLarge)
                        .clip(MaterialTheme.shapes.small)
                        .shimmerEffect()
                )
            }
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        }



        // Book button at the bottom
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            shadowElevation = TravelinDimens.ElevationLarge
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = TravelinDimens.PaddingExtraLarge,
                        vertical = TravelinDimens.PaddingSmall
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)
                ) {
                    Box(
                        modifier = Modifier
                            .width(TravelinDimens.ImageSizeMedium)
                            .height(TravelinDimens.IconSizeExtraSmall)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect()
                    )
                    Box(
                        modifier = Modifier
                            .width(TravelinDimens.ImageSizeMedium)
                            .height(TravelinDimens.IconSizeMedium)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect()
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(TravelinDimens.ButtonHeightMedium)
                        .clip(MaterialTheme.shapes.medium)
                        .shimmerEffect()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HotelDetailLoadingPreview(){
    Travelin2026ProjectLabTheme(darkTheme = true){
        HotelDetailLoading()
    }
}
