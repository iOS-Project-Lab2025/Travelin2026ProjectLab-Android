package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.Trip
import com.softserveacademy.home.domain.repository.HomeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetUpcomingTripUseCaseTest {

    private val repository = mockk<HomeRepository>()
    private val useCase = GetUpcomingTripUseCase(repository)

    @Test
    fun `given trip exists when invoke then returns success with trip`() = runTest {
        val trip = mockk<Trip>()
        coEvery { repository.getUpcomingTrip() } returns Result.success(trip)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(trip, result.getOrNull())
    }

    @Test
    fun `given no trip when invoke then returns success with null`() = runTest {
        coEvery { repository.getUpcomingTrip() } returns Result.success(null)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(null, result.getOrNull())
    }

    @Test
    fun `given error when invoke then returns failure`() = runTest {
        coEvery { repository.getUpcomingTrip() } returns Result.failure(Exception("Error"))

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals("Error", result.exceptionOrNull()?.message)
    }
}
