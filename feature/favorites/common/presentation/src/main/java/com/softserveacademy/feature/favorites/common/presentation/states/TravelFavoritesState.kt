package com.softserveacademy.feature.favorites.common.presentation.states
import com.softserveacademy.feature.favorites.common.domain.model.FavoriteItem
import com.softserveacademy.feature.favorites.common.presentation.events.FavoriteType

/**
 * Represents the UI state for the Favorites screen in compliance with MVI.
 */
data class TravelFavoritesState(
    val isLoading: Boolean = true,
    val isAuthenticated: Boolean = false,
    val selectedCategory: FavoriteType = FavoriteType.HOTEL,
    val allFavorites: List<FavoriteItem> = emptyList()
) {
    /**
     * Filters the total favorites list based on the currently selected tab/category.
     */
    val filteredFavorites: List<FavoriteItem>
        get() = allFavorites.filter { item ->
            item.type == selectedCategory.name
        }

    val isRestricted: Boolean get() = !isLoading && !isAuthenticated
    val isEmpty: Boolean get() = !isLoading && isAuthenticated && filteredFavorites.isEmpty()
    val hasContent: Boolean get() = !isLoading && isAuthenticated && filteredFavorites.isNotEmpty()
}