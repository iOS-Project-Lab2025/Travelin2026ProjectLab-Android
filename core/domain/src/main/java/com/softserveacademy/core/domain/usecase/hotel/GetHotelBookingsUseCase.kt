package com.softserveacademy.core.domain.usecase.hotel

import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.core.domain.repository.HotelBookingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all hotel bookings.
 */
class GetHotelBookingsUseCase @Inject constructor(
    private val repository: HotelBookingRepository,
) {
    operator fun invoke(): Flow<List<HotelBooking>> {
        return repository.getBookings()
    }
}
