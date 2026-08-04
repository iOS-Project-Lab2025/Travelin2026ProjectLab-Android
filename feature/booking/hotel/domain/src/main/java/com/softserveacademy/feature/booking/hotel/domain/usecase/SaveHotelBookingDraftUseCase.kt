package com.softserveacademy.feature.booking.hotel.domain.usecase

import com.softserveacademy.feature.booking.hotel.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.hotel.domain.repository.HotelBookingDraftRepository
import javax.inject.Inject

/**
 * Use case for saving a hotel booking draft.
 */
class SaveHotelBookingDraftUseCase @Inject constructor(
    private val repository: HotelBookingDraftRepository
) {
    suspend operator fun invoke(draft: HotelBookingDraft) {
        repository.saveDraft(draft)
    }
}
