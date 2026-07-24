package com.softserveacademy.core.data.repository

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelDetails
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.core.domain.model.HotelRoomAmenity
import com.softserveacademy.core.domain.model.IncludedItem
import com.softserveacademy.core.domain.repository.HotelRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.seconds
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

    private val _hotelDetails = MutableStateFlow(listOf(
        HotelDetails(
            id = 1,
            minimumPrice = 50,
            imageList = previewImages,
            name = "Swiss-Belhotel Rainforest",
            address = "Jl. Sunset Road No. 101, Kuta, Bali, Indonesia",
            star = 4,
            image = previewImages,
            numberOfReviews = 120,
            rating = 4.5,
            description = "Experience luxury in the heart of Bali with world-class amenities and breathtaking views.",
            includedItems = listOf(
                IncludedItem.BuffetBreakfast,
                IncludedItem.FreeWifi,
                IncludedItem.Pool,
                IncludedItem.AcUnit
            ),
            latitude = 1.35,
            longitude = 103.87,
            rooms = createMockRooms(1)
        ),
        HotelDetails(
            id = 2,
            minimumPrice = 120,
            imageList = previewImages1,
            name = "Discovery Kartika Plaza",
            address = "Jl. Kartika Plaza, Kuta, Bali",
            star = 5,
            image = previewImages1,
            numberOfReviews = 340,
            rating = 1.5,
            description = "Premier beachfront resort offering exceptional service and stunning ocean views. Our resort features multiple infinity pools, a world-class spa, and fine dining options that cater to every palate. Each room is meticulously designed with local artistic touches and modern comforts to ensure a truly unforgettable stay in paradise. Guests can enjoy a wide array of activities, from water sports on the private beach to yoga sessions at sunrise. Our dedicated concierge team is always on hand to curate personalized experiences, ensuring that every moment of your vacation is perfect. Whether you're seeking a romantic getaway or a fun-filled family holiday, our resort provides the ultimate sanctuary for relaxation and adventure alike.",
            includedItems = listOf(
                IncludedItem.FreeWifi,
                IncludedItem.FitnessCenter,
                IncludedItem.Pool,
                IncludedItem.RoomService,
                IncludedItem.BuffetBreakfast,
                IncludedItem.SelfParking,
                IncludedItem.AcUnit
            ),
            latitude = 1.35,
            longitude = 103.87,
            rooms = createMockRooms(2)
        )
    ))

    override suspend fun getHotelById(id: Int): HotelDetails {
        delay(1.seconds)
        return _hotelDetails.value.find { it.id == id } ?: throw NoSuchElementException("Hotel with id $id not found")
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
