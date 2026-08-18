package com.softserveacademy.core.domain.usecase.tour

import com.softserveacademy.core.domain.model.TourBooking
import com.softserveacademy.core.domain.repository.TourBookingRepository
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for saving a tour booking.
 */
class SaveTourBookingUseCase @Inject constructor(
    private val repository: TourBookingRepository
) {
    suspend operator fun invoke(booking: TourBooking): AppResult<Unit> {
        return repository.saveBooking(booking)
    }
}