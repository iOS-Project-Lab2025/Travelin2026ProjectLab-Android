package com.softserveacademy.core.domain.usecase.hotel

import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.core.domain.repository.HotelBookingRepository
import com.softserveacademy.core.error.extension.flatMap
import com.softserveacademy.core.error.extension.map
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for retrieving available hotel rooms by checking the bookings register.
 */
class GetAvailableRoomsUseCase @Inject constructor(
    private val bookingRepository: HotelBookingRepository,
    private val getFilterRoomsUseCase: GetFilterRoomsUseCase
) {
    suspend operator fun invoke(
        hotelId: String,
        checkInDate: Long,
        checkOutDate: Long,
        guestCount: Int,
        allowPets: Boolean
    ): AppResult<List<HotelRoom>> {
        return bookingRepository.getBookings().flatMap { bookings ->
            getFilterRoomsUseCase(hotelId, guestCount, allowPets).map { filteredRooms ->
                filteredRooms.filter { room ->
                    val bookedCount = bookings.count { booking ->
                        booking.roomId == room.id &&
                                booking.status != BookingStatus.CANCELLED &&
                                checkInDate < booking.checkOut &&
                                booking.checkIn < checkOutDate
                    }
                    (room.totalRooms - bookedCount) > 0
                }
            }
        }
    }
}
