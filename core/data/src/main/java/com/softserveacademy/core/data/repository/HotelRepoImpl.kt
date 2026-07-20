package com.softserveacademy.core.data.repository

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.core.domain.model.HotelRoomAmenity
import com.softserveacademy.core.domain.repository.HotelRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data class representing a booking.
 * @param roomId The ID of the booked room.
 * @param checkInDate The check-in date in milliseconds.
 * @param checkOutDate The check-out date in milliseconds.
 */
data class Booking(
    val roomId: Int,
    val checkInDate: Long,
    val checkOutDate: Long
)

/**
 * Test implementation of the HotelRepo interface that mock fetch data.
 */
@Singleton
class HotelRepoImpl @Inject constructor() : HotelRepo {
    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    private val _hotels = MutableStateFlow(listOf(
        Hotel(
            id = 1,
            name = "Swiss-Belhotel Rainforest",
            address = "Jl. Sunset Road No. 101, Kuta, Bali, Indonesia",
            star = 4,
            userRating = 4.5,
            pricePerNight = 50,
            image = previewImages,
            imagesList = previewImages,
            rooms = createMockRooms(1)
        ),
        Hotel(
            id = 2,
            name = "Discovery Kartika Plaza",
            address = "Jl. Kartika Plaza, Kuta, Bali",
            star = 5,
            userRating = 1.5,
            pricePerNight = 120,
            image = previewImages1,
            imagesList = previewImages1,
            rooms = createMockRooms(2)
        )
    ))

    override suspend fun getHotelById(id: Int?): Hotel {
        return _hotels.value.find { it.id == id } ?: _hotels.value.first()
    }

    override suspend fun getHotels(): List<Hotel> {
        return _hotels.value
    }

    override suspend fun getHotelRooms(hotelId: Int, checkInDate: Long, checkOutDate: Long): List<HotelRoom> {
        val baseRooms = _hotels.value.find { it.id == hotelId }?.rooms ?: emptyList()
        return baseRooms.map { room ->
            val bookedCount = _bookings.value.count { booking ->
                booking.roomId == room.id && 
                checkInDate < booking.checkOutDate && 
                booking.checkInDate < checkOutDate
            }
            val available = (room.totalRooms - bookedCount).coerceAtLeast(0)
            room.copy(
                availableRooms = available,
                isAvailable = available > 0
            )
        }
    }

    override suspend fun reserveRoom(hotelId: Int, roomId: Int, checkInDate: Long, checkOutDate: Long) {
        _bookings.update { currentBookings ->
            currentBookings + Booking(roomId, checkInDate, checkOutDate)
        }
    }

    private fun createMockRooms(hotelId: Int): List<HotelRoom> {
        return listOf(
            HotelRoom(
                id = hotelId * 10 + 1,
                type = "Deluxe Suite, King Size Bed",
                description = "Volcano in East Java",
                maxOccupancy = "1-2 persons",
                bedType = "1 King bed",
                bedCount = 1,
                amenities = listOf(HotelRoomAmenity.BREAKFAST, HotelRoomAmenity.WIFI),
                pricePerNight = 170,
                images = roomPreviewImages,
                totalRooms = 1,
                availableRooms = 5,
                isAvailable = true
            ),
            HotelRoom(
                id = hotelId * 10 + 2,
                type = "Standard Suite, Queen Size Bed",
                description = "Volcano in East Java",
                maxOccupancy = "1-2 persons",
                bedType = "1 Queen bed",
                bedCount = 1,
                amenities = listOf(HotelRoomAmenity.BREAKFAST, HotelRoomAmenity.WIFI),
                pricePerNight = 150,
                images = roomPreviewImages,
                totalRooms = 3,
                availableRooms = 3,
                isAvailable = true
            ),
            HotelRoom(
                id = hotelId * 10 + 3,
                type = "Family Room, 2 Double Beds",
                description = "Garden View",
                maxOccupancy = "1-4 persons",
                bedType = "2 Double beds",
                bedCount = 2,
                amenities = listOf(HotelRoomAmenity.BREAKFAST, HotelRoomAmenity.WIFI, HotelRoomAmenity.AC),
                pricePerNight = 250,
                images = roomPreviewImages,
                totalRooms = 2,
                availableRooms = 2,
                isAvailable = true
            )
        )
    }
}

private val previewImages = listOf(
    "https://picsum.photos/id/237/200/300",
    "https://picsum.photos/id/238/200/300",
    "https://picsum.photos/id/1020/800/600",
    "https://picsum.photos/id/537/200/300",
    "https://picsum.photos/id/134/200/300",
    "https://images.unsplash.com/photo-1582719508461-905c673771fd",
    "https://images.unsplash.com/photo-1582719508461-905c673771fd",
    "https://images.unsplash.com/photo-1582719508461-905c673771fd"
)

private val previewImages1 = listOf(
    "https://picsum.photos/id/137/200/300",
    "https://picsum.photos/id/138/200/300",
    "https://picsum.photos/id/20/800/600",
    "https://picsum.photos/id/937/200/300",
    "https://picsum.photos/id/324/200/300",
    "https://picsum.photos/id/643/200/300",
    "https://picsum.photos/id/682/200/300",
    "https://picsum.photos/id/111/200/300",
    "https://picsum.photos/id/152/200/300"
)

private val roomPreviewImages = listOf(
    "https://picsum.photos/id/137/200/300",
    "https://picsum.photos/id/138/200/300",
    "https://picsum.photos/id/20/800/600",
    "https://picsum.photos/id/937/200/300",
    "https://picsum.photos/id/324/200/300",
)
