package com.softserveacademy.feature.booking.domain.repository

import com.softserveacademy.feature.booking.domain.model.HotelBookingDraft

/**
 * Interface for managing booking drafts.
 * This allows persisting the draft state even when the navigation graph is popped from the backstack.
 */
interface BookingRepository {
    /**
     * Saves the hotel booking draft.
     * @param draft The draft to save.
     */
    suspend fun saveHotelBookingDraft(draft: HotelBookingDraft)

    /**
     * Retrieves the hotel booking draft for a specific hotel.
     * @param hotelId The ID of the hotel.
     * @return The saved draft, or null if none exists.
     */
    suspend fun getHotelBookingDraft(hotelId: String): HotelBookingDraft?
}