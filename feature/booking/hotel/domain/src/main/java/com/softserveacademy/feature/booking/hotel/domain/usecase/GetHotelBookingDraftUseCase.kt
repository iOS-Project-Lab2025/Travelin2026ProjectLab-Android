package com.softserveacademy.feature.booking.hotel.domain.usecase

import com.softserveacademy.feature.booking.hotel.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.hotel.domain.repository.HotelBookingDraftRepository
import javax.inject.Inject

/**
 * Use case for retrieving a hotel booking draft.
 */
class GetHotelBookingDraftUseCase @Inject constructor(
    private val repository: HotelBookingDraftRepository
) {
    suspend operator fun invoke(hotelId: String): HotelBookingDraft? {
        return repository.getDraft(hotelId)
    }
}
