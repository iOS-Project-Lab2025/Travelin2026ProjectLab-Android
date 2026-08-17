package com.softserveacademy.feature.booking.tour.domain.usecase

import com.softserveacademy.feature.booking.tour.domain.repository.TourBookingDraftRepository
import javax.inject.Inject

class ClearTourBookingDraftUseCase @Inject constructor(
    private val repository: TourBookingDraftRepository
) {
    suspend operator fun invoke(tourId: String) {
        repository.clearDraft(tourId)
    }
}
