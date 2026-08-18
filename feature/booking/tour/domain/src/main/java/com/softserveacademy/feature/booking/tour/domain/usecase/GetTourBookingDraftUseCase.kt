package com.softserveacademy.feature.booking.tour.domain.usecase

import com.softserveacademy.feature.booking.tour.domain.model.TourBookingDraft
import com.softserveacademy.feature.booking.tour.domain.repository.TourBookingDraftRepository
import javax.inject.Inject

class GetTourBookingDraftUseCase @Inject constructor(
    private val repository: TourBookingDraftRepository
) {
    suspend operator fun invoke(tourId: String): TourBookingDraft? {
        return repository.getDraft(tourId)
    }
}
