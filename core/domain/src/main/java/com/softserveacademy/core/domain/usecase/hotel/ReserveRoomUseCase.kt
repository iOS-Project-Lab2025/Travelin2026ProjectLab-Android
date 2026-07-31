package com.softserveacademy.core.domain.usecase.hotel

import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for reserving a hotel room.
 */
class ReserveRoomUseCase @Inject constructor(
    private val repository: HotelRepo
) {
    suspend operator fun invoke(
        hotelId: Int,
        roomId: Int,
        checkInDate: Long,
        checkOutDate: Long
    ): AppResult<Unit> {
        return repository.reserveRoom(hotelId, roomId, checkInDate, checkOutDate)
    }
}