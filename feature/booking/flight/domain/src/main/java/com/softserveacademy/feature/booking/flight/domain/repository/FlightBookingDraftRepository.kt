package com.softserveacademy.feature.booking.flight.domain.repository

import com.softserveacademy.feature.booking.flight.domain.model.FlightBookingDraft
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for persisting the temporary flight selection state.
 * Ensures the booking process can survive process death or screen rotations.
 */
interface FlightBookingDraftRepository {
    /**
     * Persists a snapshot of the current booking process.
     *
     * @param draft The selection state to save.
     */
    suspend fun saveDraft(draft: FlightBookingDraft)

    /**
     * Observes the current selection state.
     *
     * @return A Flow emitting the latest saved draft or null if none exists.
     */
    fun getDraft(): Flow<FlightBookingDraft?>

    /**
     * Resets the booking process by deleting the persisted draft.
     */
    suspend fun clearDraft()
}