package com.softserveacademy.feature.favorites.common.presentation.events
import com.softserveacademy.feature.favorites.common.domain.model.FavoriteItem

/**
 * Represents supported favorite item categories (e.g., Hotels, Flights, Trips).
 */
enum class FavoriteType {
    HOTEL,
    FLIGHT,
    TRIP
}

/**
 * Defines UI events for the Favorites feature using the MVI pattern.
 */
sealed interface TravelFavoritesEvent {

    /**
     * @property id The unique identifier of the clicked item.
     * @property type The category type of the favorite item ([FavoriteType]).
     */
    data class OnFavoriteItemClick(
        val id: String,
        val type: FavoriteType
    ) : TravelFavoritesEvent

    /**
     * @property item The [FavoriteItem] to be removed.
     */
    data class OnRemoveFavorite(
        val item: FavoriteItem
    ) : TravelFavoritesEvent

    /**
     * @property type The selected category tab ([FavoriteType]).
     */
    data class OnCategorySelected(
        val type: FavoriteType
    ) : TravelFavoritesEvent

    /**
     * Triggered when an unauthenticated user clicks the Sign In button.
     */
    data object OnSignInClick : TravelFavoritesEvent

    /**
     * Triggered when the user clicks the Go Back button on an empty screen.
     */
    data object OnGoBackClick : TravelFavoritesEvent

    /**
     * Triggered to refresh the favorites list.
     */
    data object OnRefresh : TravelFavoritesEvent
}