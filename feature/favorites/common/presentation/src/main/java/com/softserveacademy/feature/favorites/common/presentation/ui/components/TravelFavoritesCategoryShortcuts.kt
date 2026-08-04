package com.softserveacademy.feature.favorites.common.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.theme.FlightIcon
import com.softserveacademy.core.presentation.design_system.theme.Gray10
import com.softserveacademy.core.presentation.design_system.theme.Gray40
import com.softserveacademy.core.presentation.design_system.theme.HotelIcon
import com.softserveacademy.core.presentation.design_system.theme.TicketIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme

data class FavoriteCategoryShortcut(
    val id: String,
    val label: String,
    val icon: ImageVector
)
/**
 * Category shortcuts for the Favorites screen.
 *
 * @param categories List of categories to display.
 * @param isEnabled Specifies whether the shortcuts are enabled (displays green with alpha)
 *                  or disabled/empty (displays a Gray40 background).
 * @param onCategoryClick Action to take when a button is clicked.
 */
@Composable
fun TravelFavoritesCategoryShortcuts(
    categories: List<FavoriteCategoryShortcut>,
    isEnabled: Boolean = true,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isEnabled) {
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
    } else {
        Gray40
    }

    val iconColor = if (isEnabled) {
        MaterialTheme.colorScheme.secondary
    } else {
        Gray10
    }

    val textColor = if (isEnabled) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { category ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TravelIconButton(
                    icon = category.icon,
                    onClick = { if (isEnabled) onCategoryClick(category.id) },
                    enabled = true,
                    backgroundColor = backgroundColor,
                    iconColor = iconColor
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = category.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor
                )
            }
        }
    }
}

// ============================================================================
// PREVIEWS
// ============================================================================

@Preview(showBackground = true, name = "Enabled (Green) State")
@Composable
private fun TravelFavoritesCategoryShortcutsEnabledPreview() {
    val sampleCategories = listOf(
        FavoriteCategoryShortcut("flights", "Flights", FlightIcon),
        FavoriteCategoryShortcut("hotels", "Hotels", HotelIcon),
        FavoriteCategoryShortcut("tours", "Tours", TicketIcon)
    )

    Travelin2026ProjectLabTheme {
        TravelFavoritesCategoryShortcuts(
            categories = sampleCategories,
            isEnabled = true,
            onCategoryClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Disabled (Gray40 / Gray10) State")
@Composable
private fun TravelFavoritesCategoryShortcutsDisabledPreview() {
    val sampleCategories = listOf(
        FavoriteCategoryShortcut("flights", "Flights", FlightIcon),
        FavoriteCategoryShortcut("hotels", "Hotels", HotelIcon),
        FavoriteCategoryShortcut("tours", "Tours", TicketIcon)
    )

    Travelin2026ProjectLabTheme {
        TravelFavoritesCategoryShortcuts(
            categories = sampleCategories,
            isEnabled = false,
            onCategoryClick = {}
        )
    }
}