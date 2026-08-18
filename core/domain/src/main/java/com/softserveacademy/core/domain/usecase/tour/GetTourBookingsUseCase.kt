package com.softserveacademy.core.domain.usecase.tour

import com.softserveacademy.core.domain.model.TourBooking
import com.softserveacademy.core.domain.repository.TourBookingRepository
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for retrieving all tour bookings.
 */
class GetTourBookingsUseCase @Inject constructor(
    private val repository: TourBookingRepository,
) {
    suspend operator fun invoke(): AppResult<List<TourBooking>> {
        return repository.getBookings()
    }
}
