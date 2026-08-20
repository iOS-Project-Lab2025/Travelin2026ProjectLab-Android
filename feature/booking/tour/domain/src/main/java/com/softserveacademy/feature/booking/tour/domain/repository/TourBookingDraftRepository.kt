package com.softserveacademy.feature.booking.tour.domain.repository

import com.softserveacademy.feature.booking.tour.domain.model.TourBookingDraft

/**
 * Interface for managing tour booking drafts.
 */
interface TourBookingDraftRepository {
    suspend fun getDraft(tourId: String): TourBookingDraft?
    suspend fun saveDraft(draft: TourBookingDraft)
    suspend fun clearDraft(tourId: String)
}
