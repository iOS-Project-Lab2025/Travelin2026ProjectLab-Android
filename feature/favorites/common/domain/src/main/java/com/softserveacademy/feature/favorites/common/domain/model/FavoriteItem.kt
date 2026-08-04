package com.softserveacademy.feature.favorites.common.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteItem(
    val id: String,
    val title: String,
    val location: String,
    val rating: Double,
    val type: String,
    val price: Int,
    val imageUrl: String,
    val addedAt: Long
) {
}
