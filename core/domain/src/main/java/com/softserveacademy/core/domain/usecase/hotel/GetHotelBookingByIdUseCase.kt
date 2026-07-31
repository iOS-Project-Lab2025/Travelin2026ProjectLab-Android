package com.softserveacademy.core.domain.usecase.hotel

import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.core.domain.repository.HotelBookingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving a hotel booking by its ID.
 */
class GetHotelBookingByIdUseCase @Inject constructor(
    private val repository: HotelBookingRepository,
) {
    operator fun invoke(bookingId: String): Flow<HotelBooking?> {
        return repository.getBookingById(bookingId)
    }
}
