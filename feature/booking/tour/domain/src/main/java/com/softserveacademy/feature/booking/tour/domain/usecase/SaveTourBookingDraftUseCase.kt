package com.softserveacademy.feature.booking.tour.domain.usecase

import com.softserveacademy.feature.booking.tour.domain.model.TourBookingDraft
import com.softserveacademy.feature.booking.tour.domain.repository.TourBookingDraftRepository
import javax.inject.Inject

class SaveTourBookingDraftUseCase @Inject constructor(
    private val repository: TourBookingDraftRepository
) {
    suspend operator fun invoke(draft: TourBookingDraft) {
        repository.saveDraft(draft)
    }
}
