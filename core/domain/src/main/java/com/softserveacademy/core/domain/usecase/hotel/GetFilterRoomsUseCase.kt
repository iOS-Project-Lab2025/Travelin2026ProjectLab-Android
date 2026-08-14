package com.softserveacademy.core.domain.usecase.hotel

import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.core.error.extension.map
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for filtering hotel rooms based on guest count and pet allowance.
 */
class GetFilterRoomsUseCase @Inject constructor(
    private val getHotelRoomsUseCase: GetHotelRoomsUseCase
) {
    suspend operator fun invoke(
        hotelId: String,
        guestCount: Int,
        allowPets: Boolean
    ): AppResult<List<HotelRoom>> {
        return getHotelRoomsUseCase(hotelId).map { rooms ->
            rooms.filter { room ->
                room.maxOccupancy >= guestCount && (if (allowPets) room.allowPets else true)
            }
        }
    }
}
