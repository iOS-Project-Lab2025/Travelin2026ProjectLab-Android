package com.softserveacademy.feature.favorites.common.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.components.TravelTextActionButton
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.feature.favorites.common.domain.model.FavoriteItem

@Composable
fun TravelFavoritesSection(
    title: String,
    items: List<FavoriteItem>,
    onSeeAllClick: () -> Unit,
    onCardClick: (FavoriteItem) -> Unit,
    onRemoveClick: (FavoriteItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            TravelTextActionButton(
                text = "See all",
                onClick = onSeeAllClick
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(items = items, key = { it.id }) { item ->
                FavoriteItemCard(
                    favoriteItem = item,
                    onCardClick = onCardClick,
                    onRemoveClick = onRemoveClick,
                    modifier = Modifier.width(180.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Favorites Section Preview")
@Composable
private fun TravelFavoritesSectionPreview() {
    val mockItems = listOf(
        FavoriteItem(
            id = "1",
            title = "Mount Bromo",
            location = "Volcano in East Java",
            rating = 4.9,
            price = 150.0,
            imageUrl = "",
            type = "TOURS",
            addedAt = System.currentTimeMillis()
        ),
        FavoriteItem(
            id = "2",
            title = "Labengki Sombori",
            location = "Islands in Sulawesi",
            rating = 4.8,
            price = 250.0,
            imageUrl = "",
            type = "TOURS",
            addedAt = System.currentTimeMillis()
        )
    )

    Travelin2026ProjectLabTheme {
        TravelFavoritesSection(
            title = "Tours",
            items = mockItems,
            onSeeAllClick = {},
            onCardClick = {},
            onRemoveClick = {}
        )
    }
}