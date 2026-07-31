package com.softserveacademy.core.domain.usecase.hotel

import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.core.domain.repository.HotelBookingRepository
import javax.inject.Inject

/**
 * Use case for saving a hotel booking.
 */
class SaveHotelBookingUseCase @Inject constructor(
    private val repository: HotelBookingRepository
) {
    suspend operator fun invoke(booking: HotelBooking) {
        repository.saveBooking(booking)
    }
}