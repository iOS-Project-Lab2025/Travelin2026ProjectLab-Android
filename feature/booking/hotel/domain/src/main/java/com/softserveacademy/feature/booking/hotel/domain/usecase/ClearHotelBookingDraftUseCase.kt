package com.softserveacademy.feature.booking.hotel.domain.usecase

import com.softserveacademy.feature.booking.hotel.domain.repository.HotelBookingDraftRepository
import javax.inject.Inject

/**
 * Use case for clearing a hotel booking draft.
 */
class ClearHotelBookingDraftUseCase @Inject constructor(
    private val repository: HotelBookingDraftRepository
) {
    suspend operator fun invoke(hotelId: String) {
        repository.clearDraft(hotelId)
    }
}
