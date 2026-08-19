package com.softserveacademy.feature.favorites.tours.domain.model

import com.softserveacademy.core.domain.model.Tour

data class FavoriteTour(
    val id: String,
    val tour: Tour,
    val isAvailable: Boolean = true,
    val category: String = "Adventure"
)
