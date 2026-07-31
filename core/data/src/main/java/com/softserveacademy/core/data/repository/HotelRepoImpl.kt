package com.softserveacademy.core.data.repository

import com.softserveacademy.core.data.api.HotelApiService
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelDetails
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.error.mapper.ExceptionMapper
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.core.error.util.safeCall
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
 * Implementation of the HotelRepo interface that fetches data from a real API.
 */
@Singleton
class HotelRepoImpl @Inject constructor(
    private val hotelApiService: HotelApiService,
    private val mapper: ExceptionMapper
) : HotelRepo {
    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())

    override suspend fun getHotelById(id: Int): AppResult<HotelDetails> = safeCall(mapper) {
        hotelApiService.getHotelById(id)
    }

    override suspend fun getHotels(): AppResult<List<Hotel>> = safeCall(mapper) {
        hotelApiService.getHotels()
    }

    override suspend fun getHotelRooms(hotelId: Int, checkInDate: Long, checkOutDate: Long, guestCount: Int): AppResult<List<HotelRoom>> = safeCall(mapper) {
        val hotel = hotelApiService.getHotelById(hotelId)
        val baseRooms = hotel.rooms
        baseRooms
            .filter { it.maxOccupancy >= guestCount }
            .map { room ->
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

    override suspend fun reserveRoom(hotelId: Int, roomId: Int, checkInDate: Long, checkOutDate: Long): AppResult<Unit> = safeCall(mapper) {
        _bookings.update { currentBookings ->
            currentBookings + Booking(roomId, checkInDate, checkOutDate)
        }
    }
}
