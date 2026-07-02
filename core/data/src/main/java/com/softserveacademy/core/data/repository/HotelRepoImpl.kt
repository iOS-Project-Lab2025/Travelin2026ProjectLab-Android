package com.softserveacademy.core.data.repository

import com.softserveacademy.core.data.R
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.repository.HotelRepo
import javax.inject.Inject

/**
 * Test implementation of the HotelRepo interface that mock fetch data.
 */
class HotelRepoImpl @Inject constructor() : HotelRepo {
    private val hotels = listOf(
        Hotel(
            id = 1,
            name = "Swiss-Belhotel Rainforest",
            address = "Jl. Sunset Road No. 101, Kuta, Bali, Indonesia",
            star = 4,
            pricePerNight = 50,
            image = R.drawable.test_place
        ),
        Hotel(
            id = 2,
            name = "Discovery Kartika Plaza",
            address = "Jl. Kartika Plaza, Kuta, Bali",
            star = 5,
            pricePerNight = 120,
            image = R.drawable.test_hotel
        )
    )

    override suspend fun getHotelById(id: Int?): Hotel {
        return hotels.find { it.id == id } ?: hotels.first()
    }
}