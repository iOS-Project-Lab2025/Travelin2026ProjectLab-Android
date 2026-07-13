package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelStar
import com.softserveacademy.core.presentation.design_system.theme.Gray40
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * A custom rating bar component that displays stars based on a
 * rating value, supporting partial fills.
 *
 * @param rating The rating value to display from 0.0 to 5.0.
 * @param starSize The size of each star icon.
 */
@Composable
fun TravelRatingBar(
    rating: Double,
    starSize: Dp = TravelinDimens.IconSizeExtraSmall
) {
    Row {
        for (i in 1..5) {
            val fill = when {
                rating >= i -> 1f
                rating > i - 1 -> (rating - (i - 1)).toFloat()
                else -> 0f
            }
            Box(
                modifier = Modifier
                .size(starSize)
            ) {
                if (fill == 1f) {
                    TravelStar(starSize)
                } else if (fill > 0f) {
                    TravelStar(starSize, starColor = Gray40)
                    TravelStar(starSize, iconFill = fill)
                } else {
                    TravelStar(starSize, starColor = Gray40)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun TravelHotelDetailScreenPreview() {
    Travelin2026ProjectLabTheme {
        TravelRatingBar(3.7)
    }
}
