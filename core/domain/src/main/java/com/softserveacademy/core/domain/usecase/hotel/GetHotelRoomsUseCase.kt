package com.softserveacademy.core.domain.usecase.hotel

import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for retrieving available hotel rooms.
 */
class GetHotelRoomsUseCase @Inject constructor(
    private val repository: HotelRepo
) {
    suspend operator fun invoke(
        hotelId: Int,
        checkInDate: Long,
        checkOutDate: Long,
        guestCount: Int
    ): AppResult<List<HotelRoom>> {
        return repository.getHotelRooms(hotelId, checkInDate, checkOutDate, guestCount)
    }
}