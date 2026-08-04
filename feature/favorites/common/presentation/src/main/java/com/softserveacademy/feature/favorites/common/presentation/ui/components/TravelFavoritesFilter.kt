package com.softserveacademy.feature.favorites.common.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme

/**
 * Componente horizontal de selección de categorías para la pantalla de Favoritos.
 *
 * @param categories Lista de nombres de las categorías a mostrar.
 * @param selectedCategory Categoría actualmente seleccionada.
 * @param onCategorySelected Callback ejecutado al presionar un chip.
 * @param modifier Modificador de Compose para personalizar el diseño externo.
 */
@Composable
fun TravelFavoritesCategoryFilter(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
    ) {
        items(categories) { category ->
            val isSelected = category.equals(selectedCategory, ignoreCase = true)

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ============================================================================
// PREVIEWS
// ============================================================================

@Preview(showBackground = true, name = "Category Filter Preview")
@Composable
private fun TravelFavoritesCategoryFilterPreview() {
    val categories = listOf("All", "Hotels", "Flights", "Trips")
    var selectedCategory by remember { mutableStateOf("Hotels") }

    Travelin2026ProjectLabTheme {
        TravelFavoritesCategoryFilter(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
        )
    }
}