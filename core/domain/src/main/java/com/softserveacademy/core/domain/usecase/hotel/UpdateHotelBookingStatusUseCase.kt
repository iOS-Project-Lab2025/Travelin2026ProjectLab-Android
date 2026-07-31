package com.softserveacademy.core.domain.usecase.hotel

import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.repository.HotelBookingRepository
import javax.inject.Inject

/**
 * Use case for updating hotel booking status.
 */
class UpdateHotelBookingStatusUseCase @Inject constructor(
    private val repository: HotelBookingRepository
) {
    suspend operator fun invoke(bookingId: String, status: BookingStatus) {
        repository.updateBookingStatus(bookingId, status)
    }
}