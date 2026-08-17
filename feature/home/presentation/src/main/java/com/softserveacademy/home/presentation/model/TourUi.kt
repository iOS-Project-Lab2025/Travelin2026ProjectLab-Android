package com.softserveacademy.home.presentation.model

import com.softserveacademy.core.domain.model.RatePerParticipant

data class TourUi(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val location: String,
    val rating: Double,
    val rates: RatePerParticipant,
    val duration: String
)
