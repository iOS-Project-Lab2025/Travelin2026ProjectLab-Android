package com.softserveacademy.core.domain.usecase.tour

import com.softserveacademy.core.domain.model.TourBooking
import com.softserveacademy.core.domain.repository.TourBookingRepository
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for retrieving a tour booking by its ID.
 */
class GetTourBookingByIdUseCase @Inject constructor(
    private val repository: TourBookingRepository,
) {
    suspend operator fun invoke(bookingId: String): AppResult<TourBooking?> {
        return repository.getBookingById(bookingId)
    }
}
