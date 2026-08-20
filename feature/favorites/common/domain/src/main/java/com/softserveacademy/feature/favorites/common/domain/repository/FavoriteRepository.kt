package com.softserveacademy.feature.favorites.common.domain.repository

import com.softserveacademy.feature.favorites.common.domain.model.FavoriteItem
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    /**
     * Returns a continuous flow of the list of saved favorite items.
     */
    fun getFavorites(): Flow<List<FavoriteItem>>
    /**
     * Adds a new item to the favorites list.
     */
    suspend fun addFavorite(item: FavoriteItem)
    /**
     * Removes an item from the favorites list by its ID.
     */
    suspend fun removeFavorite(id: String)
    /**
     * Checks whether a specific item is already a favorite.
     */
    suspend fun isFavorite(id: String): Boolean
}