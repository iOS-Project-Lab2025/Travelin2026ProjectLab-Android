package com.softserveacademy.home.presentation.ui.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.theme.ArrowLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.HeartLineIcon
import com.softserveacademy.core.presentation.design_system.theme.ShareIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.core.presentation.design_system.theme.White100_Alpha70
import com.softserveacademy.core.presentation.design_system.theme.Green70

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
        TravelIconButton(
            icon = ArrowLeftIcon,
            onClick = onBackClick,
            iconColor = Green70,
            backgroundColor = White100_Alpha70,
            contentDescription = "Back button"
        )
        Row {
            TravelIconButton(
                icon = ShareIcon,
                onClick = onShareClick,
                iconColor = Green70,
                backgroundColor = White100_Alpha70,
                contentDescription = "Share button"
            )

            Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))

            TravelIconButton(
                icon = HeartLineIcon,
                onClick = onFavoriteClick,
                iconColor = Green70,
                backgroundColor = White100_Alpha70,
                contentDescription = "Favorite button"
            )
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