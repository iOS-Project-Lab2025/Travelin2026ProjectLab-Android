package com.softserveacademy.core.domain.usecase.tour

import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.repository.TourBookingRepository
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for updating tour booking status.
 */
class UpdateTourBookingStatusUseCase @Inject constructor(
    private val repository: TourBookingRepository
) {
    suspend operator fun invoke(bookingId: String, status: BookingStatus): AppResult<Unit> {
        return repository.updateBookingStatus(bookingId, status)
    }
}