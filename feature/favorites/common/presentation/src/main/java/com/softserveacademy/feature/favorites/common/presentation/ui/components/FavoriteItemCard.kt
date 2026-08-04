package com.softserveacademy.feature.favorites.common.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.components.TravelCardVertical
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.theme.HeartFilledIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.favorites.common.domain.model.FavoriteItem
import com.softserveacademy.feature.favorites.common.presentation.events.FavoriteType
import com.softserveacademy.feature.favorites.common.presentation.mappers.toHotel

@Composable
fun FavoriteItemCard(
    favoriteItem: FavoriteItem,
    onCardClick: (FavoriteItem) -> Unit,
    onRemoveClick: (FavoriteItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clickable { onCardClick(favoriteItem) }
    ) {
        TravelCardVertical(
            hotel = favoriteItem.toHotel()
        )

        TravelIconButton(
            icon = HeartFilledIcon,
            onClick = { onRemoveClick(favoriteItem) },
            iconColor = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(TravelinDimens.PaddingSmall)
        )
    }
}
@Preview(
    showBackground = false,
    name = "Favorite Item Card Preview",
    apiLevel = 34
)
@Composable
private fun FavoriteItemCardPreview() {
    val sampleItem = FavoriteItem(
        id = "1",
        title = "Grand Hotel Valparaíso",
        location = "Viña del Mar, Chile",
        rating = 4.8,
        price = 120,
        imageUrl = "https://picsum.photos/200",
        type = FavoriteType.HOTEL.name,
        addedAt = System.currentTimeMillis()
    )

    Travelin2026ProjectLabTheme {
        Box(
            modifier = Modifier
                .width(200.dp)
                .padding(TravelinDimens.PaddingMedium)
        ) {
            FavoriteItemCard(
                favoriteItem = sampleItem,
                onCardClick = {},
                onRemoveClick = {}
            )
        }
    }
}