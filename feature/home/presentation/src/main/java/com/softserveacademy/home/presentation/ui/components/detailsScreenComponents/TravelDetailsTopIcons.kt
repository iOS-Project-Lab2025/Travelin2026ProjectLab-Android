package com.softserveacademy.home.presentation.ui.components.detailsScreenComponents


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelArrowIconButton
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelFavoriteIconButton
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelShareIconButton
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@Composable
fun TravelDetailsTopIcons(
    isFavorite: Boolean = false,
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
        TravelArrowIconButton(onClick = onBackClick)

        Row {
            TravelShareIconButton(onClick = onShareClick)

            Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))

            TravelFavoriteIconButton(
                isFavorite = isFavorite,
                onClick = onFavoriteClick
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun TravelDetailsTopIconsPreview() {
    Travelin2026ProjectLabTheme {
        TravelDetailsTopIcons()
    }
}
