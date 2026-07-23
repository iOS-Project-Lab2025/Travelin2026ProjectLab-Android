package com.softserveacademy.feature.booking.hotel.domain.repository

import com.softserveacademy.feature.booking.hotel.domain.model.HotelBookingDraft

/**
 * Interface for managing hotel booking drafts.
 * This allows persisting the draft state even when the navigation graph is popped from the backstack.
 */
interface HotelBookingDraftRepository {
    /**
     * Retrieves the hotel booking draft for a specific hotel.
     * @param hotelId The ID of the hotel.
     * @return The saved draft, or null if none exists.
     */
    suspend fun getDraft(hotelId: String): HotelBookingDraft?

    /**
     * Saves the hotel booking draft.
     * @param draft The draft to save.
     */
    suspend fun saveDraft(draft: HotelBookingDraft)

    /**
     * Clears the hotel booking draft.
     */
    suspend fun clearDraft(hotelId: String)
}