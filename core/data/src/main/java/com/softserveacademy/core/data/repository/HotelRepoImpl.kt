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
            image = R.drawable.test_place,
            imagesList = previewImages
        ),
        Hotel(
            id = 2,
            name = "Discovery Kartika Plaza",
            address = "Jl. Kartika Plaza, Kuta, Bali",
            star = 5,
            pricePerNight = 120,
            image = R.drawable.test_hotel,
            imagesList = previewImages
        )
    )

    override suspend fun getHotelById(id: Int?): Hotel {
        return hotels.find { it.id == id } ?: hotels.first()
    }

    override suspend fun getHotels(): List<Hotel> {
        return hotels
    }
}

private val previewImages = listOf(
    "https://picsum.photos/200/300",
    "https://picsum.photos/200",
    "https://picsum.photos/id/1020/800/600",
    "https://images.unsplash.com/photo-1582719508461-905c673771fd",
    "https://images.unsplash.com/photo-1582719508461-905c673771fd",
    "https://images.unsplash.com/photo-1582719508461-905c673771fd",
    "https://images.unsplash.com/photo-1582719508461-905c673771fd",
    "https://images.unsplash.com/photo-1582719508461-905c673771fd"
    //"https://picsum.photos/id/1020/800/600"
)