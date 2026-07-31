package com.softserveacademy.feature.booking.flight.domain.repository

import com.softserveacademy.feature.booking.flight.domain.model.FlightBookingDraft
import kotlinx.coroutines.flow.Flow

/**
 * Interface for persisting and retrieving the flight booking progress.
 */
interface FlightBookingDraftRepository {
    suspend fun saveDraft(draft: FlightBookingDraft)
    fun getDraft(): Flow<FlightBookingDraft?>
    suspend fun clearDraft()
}