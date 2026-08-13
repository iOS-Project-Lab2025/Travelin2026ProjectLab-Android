package com.softserveacademy.core.domain.usecase.hotel

import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.core.domain.repository.HotelBookingRepository
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for retrieving a hotel booking by its ID.
 */
class GetHotelBookingByIdUseCase @Inject constructor(
    private val repository: HotelBookingRepository,
) {
    suspend operator fun invoke(bookingId: String): AppResult<HotelBooking?> {
        return repository.getBookingById(bookingId)
    }
}
