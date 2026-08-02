package com.softserveacademy.core.presentation.design_system.components.util.detailsScreenUtilities


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelArrowIcon
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelFavoriteIcon
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelShareIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@Composable
fun TravelHotelDetailsTopIcons(
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {}
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = TravelinDimens.PaddingMedium,
                vertical = TravelinDimens.PaddingSmall
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TravelArrowIcon(onClick = onBackClick)

        Row {
            TravelShareIcon(onClick = onShareClick)

            Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))

            TravelFavoriteIcon(onClick = onFavoriteClick)
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun TravelHotelDetailScreenPreview() {
    Travelin2026ProjectLabTheme {
        TravelHotelDetailsTopIcons()
    }
}