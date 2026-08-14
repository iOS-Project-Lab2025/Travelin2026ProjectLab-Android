package com.softserveacademy.core.domain.usecase.hotel

import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.core.domain.repository.HotelBookingRepository
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for retrieving all hotel bookings.
 */
class GetHotelBookingsUseCase @Inject constructor(
    private val repository: HotelBookingRepository,
) {
    suspend operator fun invoke(): AppResult<List<HotelBooking>> {
        return repository.getBookings()
    }
}
